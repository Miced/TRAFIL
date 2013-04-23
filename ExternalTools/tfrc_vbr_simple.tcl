###########################################################
## tfrc_vbr_simple.tcl                                   ##
## Nov 2007: for TFRC & TCP with FIFO or RED             ##
##                                                       ##
## Example tcl script for ns-2 that includes             ##
## possibilities to select trace file input as well as   ##
## CBR-RA/TFRC, RA-SVBR/TFRC and FTP/TCP traffic.        ##
## The trace file input                                  ##
## runs the Evalvid-RA engine.                           ##
##                                                       ##
## Note: select YOUR video input set of files by         ##
## modifying the name "st_Inconv1.yuv_Q" to match your   ##
## content in the "Create Evalvid-RA traffic sources"    ##
## section. Set "simtime" at least the length of your    ##
## video, and set frames_per_second to your contents     ##
## value.                                                ##
###########################################################
# Version for interfacing to Evalvid_RA @ both frame      #
# and GOP level. Uses TFRC CC and RED routers w/ ECN      #
###########################################################
set ns [new Simulator]
set simtime 42       ;# Simulation time in seconds
set MON_QUEUE_NUM 0
set queueTime 0.4     ;# (seconds) worth of draining a full router queue (size of queue buffer)
set numb_vbr 32        ;# Number of VBR over TFRC flows
set numb_ftp 32       ;# Number of FTP over TCP flows
set numb_web 120      ;# Number of Web over TCP flows

# Source maximum rates
set cbr_true 0     ;# 0 = RA-SVBR, 1 = CBR_RA
set vbr_rate 1.0e6  ;# Evalvid-RA video: select base rate on VBR sources (includes IP overhead)
set cbr_rate 2.5e6  ;# Rate adaptiv CBR sources max rate

set max_fragmented_size   972 ;# Evalvid-RA, MTU
set frames_per_second 29.97   ;# Evalvid-RA, All video is treated with equal fps in current release

# Link capacities
set access_cap 3Mb       ;# access link capacities
set bottleneck_cap 40.0e6 ;# between r0 and r1

# Link propagation delays
set access_del     0.005
set bottleneck_del 0.010

set q_variants 30 ;# Evalvid-RA, number of quantiser scale range
#add udp header(8 bytes) and IP header (20bytes):
#set packetSize	[expr $max_fragmented_size + 28]
#add DCCP/TFRC header(12+4=16 bytes) and IP header (20bytes):
set packetSize	[expr $max_fragmented_size + 36]

set p_aqm_true 2   ;# 0 = droptail, >0 = RED
set mean_psize $packetSize
set count_bytes 1 ;# 1 = true (qib_ true), 0 = false (count packets)
#
#
# This is the size of the queue buffer:
set queueSize [expr ($bottleneck_cap*$queueTime)/(8*$mean_psize)] 

# target RED queue equilibrium:
set targetD [expr $queueTime * 0.5]

$ns color 0 blue
$ns color 1 red
$ns color 2 green
$ns color 4 yellow
$ns color 3 chocolate

# SETTING GLOBAL TFRC DEFAULTS:
Agent/TFRC set ss_changes_ 1 ; 	# Added on 10/21/2004
Agent/TFRC set slow_increase_ 1 ; 	# Added on 10/20/2004
Agent/TFRC set rate_init_ 2 ;
Agent/TFRC set rate_init_option_ 2 ;    # Added on 10/20/2004

Agent/TFRC set SndrType_ 1 
Agent/TFRC set oldCode_ false
Agent/TFRC set packetSize_ $packetSize
Agent/TFRC set maxqueue_ 500
Agent/TFRC set printStatus_ true
Agent/TFRC set ecn_ 0 ;# Enable ECN
Agent/TFRC set useHeaders_ false
#Agent/TFRC set voip_ 1
#Agent/TFRC set voip_max_pkt_rate_ 1000 ; # In packets per second
#Agent/TCP set packetSize_ 500
#Application/Traffic/CBR set packetSize_ 500
#Agent/TCP set window_ 1000
#Agent/TCP set partial_ack_ 1

#Open the files for nam trace
#set f [open out.tr w]
#$ns trace-all $f
#set nf [open out.nam w]
#$ns namtrace-all $nf

#Open the output files for xgraph
set f0 [open out0.tr w]
set f1 [open out1.tr w]
set f2 [open out2.tr w]


#################################################################################
## Create the nodes, links and queue types for the Bottleneck of the Dumbbell
#################################################################################
set r(0) [$ns node]
set r(1) [$ns node]
if {$p_aqm_true > 0} {
    $ns simplex-link $r(0) $r(1) $bottleneck_cap $bottleneck_del RED
} else {
    $ns simplex-link $r(0) $r(1) $bottleneck_cap $bottleneck_del DropTail
}
$ns simplex-link $r(1) $r(0) $bottleneck_cap $bottleneck_del DropTail

## Access Nodes and Links for TCP
for {set i 0} {$i <  [expr $numb_ftp+$numb_vbr+$numb_web]} {incr i} { 
    set n([expr $i*2+0]) [$ns node]
    set n([expr $i*2+1]) [$ns node]
    $ns duplex-link $n([expr $i*2+0]) $r(0)  $access_cap [expr $access_del+0.000002*$i] DropTail
    $ns duplex-link $r(1) $n([expr $i*2+1])  $access_cap [expr $access_del+0.000001*$i] DropTail
}

#$ns trace-queue $n(0) $r(0) $f
#$ns trace-queue $r(1) $n(1) $f

########################################################
## Configure RED routers
########################################################
if {$p_aqm_true > 0} {
    ##
    ## Configure RED aqm queue parameters here
    ##
    set red_aqm [[$ns link $r(0) $r(1)] queue]
    $red_aqm set setbit_ true
    $red_aqm set bytes_ true
    $red_aqm set queue_in_bytes_ true
    $red_aqm set thresh_ 0
    $red_aqm set maxthresh_ 0
    $red_aqm set mean_pktsize_ 1036
    $red_aqm set q_weight_ -1
    $red_aqm set linterm_ 40
    $red_aqm set wait_ true
    $red_aqm set gentle_ true
    $red_aqm set adaptive_ true
    $red_aqm set drop_tail_ true 
    $red_aqm set targetdelay_ $targetD

    #set the queue-limit between n(0) and n(1)
    $ns queue-limit $r(0) $r(1) $queueSize
    
    ## This queue tracing stores all changes to selected parameters in file all.q:
    puts "Make RED trace variables"
    $red_aqm trace curq_        ;# trace current queue size
    $red_aqm trace prob1_       ;# trace probability of drop/mark
    $red_aqm trace ave_         ;# trace average queue in RED
    set tchan_ [open all.q w]
    $red_aqm attach $tchan_
} else { ;# or ordinary FIFO router:
    set droptail_q [[$ns link $r(0) $r(1)] queue]
    #$droptail_q trace curq_        ;# trace current queue size
    #$droptail_q trace prob_        ;# trace probability of drop/mark
    #set tchan_ [open all.q w]
    #$droptail_q attach $tchan_
   $ns queue-limit $r(0) $r(1) $queueSize
}

set f4 [open outqm.tr w]
set qmon [$ns monitor-queue $r(0) $r(1) $f4 0.01] 
#set qmon [$ns monitor-queue $r(0) $r(1) ""] 
[$ns link $r(0) $r(1)] queue-sample-timeout
$qmon set pdrops_

#############################################################
## The last procedure to run
#############################################################
proc finish {} {
    global ns f nf f0 f1 f2 f3 f4 qfile tchan2_ qmon bottleneck_cap simtime outfile
    $qmon instvar bdrops_ bdepartures_

    set utlzn [expr ($bdepartures_ * 8.0)/($bottleneck_cap * $simtime)]
    set d [expr 1.0*$bdrops_ / ($bdrops_ + $bdepartures_)]

    puts "\n#################statistics######################"
    puts "#Bytes of drops     : $bdrops_ "
    puts "#Bytes of departures: $bdepartures_ "
    puts "drops stats         : $d "
    puts "utilization         : $utlzn "

    # awkCode is for making plot of queue at bottleneck
    set awkCode {
	{
	    if ($1 == "Q" && NF>2) {
		print $2, $3 >> "temp.q";
		set end $2
	    }
	    if ($1 == "3" && NF>2) {
	    	print $2, $3 >> "tempUDP.q";
	    	set end $2
	    }
	    else if ($1 == "p" && NF>2)
	    print $2, $3 >> "temp.p";
	    else if ($1 == "a" && NF>2)
	    print $2, $3 >> "temp.a";
	    else if ($1 == "2" && NF>2)
	    print $2, $3 >> "tempUDP.p";
	}
    }

    # awkCode2 is for plotting cwnd of selected TCP source
    set awkCode2 {
	BEGIN {
	    # simple awk script to generate plot file for congestion window
	    # in a form suitable for plotting with xgraph.
	    # Lloyd Wood, July 1999.
	    # http://www.ee.surrey.ac.uk/Personal/L.Wood/ns/
	    # Arne Lie: added some modifications Sept 2004
	    # INPUT PARAMS: source must be given as s=4 in the command line, destination as d=5
	    
	    n = 0;
	}
	{
	    time = $2;
	    source = $4;
	    dest = $8;
	    cwnd = $18;

	    if (( source == s ) && (dest == d)) {
		cwnd_time[n] = time;
		cwnd_arr[n++] = cwnd;
	    }
	}
							  
	END {
	    for ( i = 0; i < n; i++ ) {
                 printf("%f %d\n", cwnd_time[i], cwnd_arr[i]);
                 #print cwnd_time[i], cwnd_arr[i] >> "cwnd_res.tr" 
				   }
	}	
    }
    # 
    # Prepare files for Queue plot
    #
    set f5 [open temp.queue w]
    puts $f5 "TitleText: Queue dynamics at selected RED ECN node"
    puts $f5 "Device: Postscript"
    
    if { [info exists qfile] } {
	close $qfile
    }
    exec rm -f temp.a temp.p temp.q tempUDP.q tempUDP.p ;# deletes old files without asking (f=force)
    exec touch temp.a temp.p temp.q tempUDP.q tempUDP.p ;# creates new empty files with correct time stamp
    exec awk $awkCode all.q          ;# all.q has been monitoring the selected RED queue
    puts $f5 \"RED_queue\"
    exec cat temp.q >@ $f5 
    puts $f5 \n\"UDP_queue\"
    exec cat tempUDP.q >@ $f5 
    puts $f5 \n\"RED_drop_probability\"
    exec cat temp.p >@ $f5
    puts $f5 \n\"UDP_drop_probability\"
    exec cat tempUDP.p >@ $f5
    puts $f5 \n\"RED_averageQueue\"
    exec cat temp.a >@ $f5
    close $f5
# Displays queue instantaneous together with prob. of drop and ECF value (averaged drop)
    #exec xgraph -bb -tk -x time -y queue -nl -m temp.queue -ly 0,1.1 & 
    #exec xgraph -bb -x time -y queue  temp.queue & 

    $ns flush-trace
    #close $f
#   close $nf
    close $f0
    close $f1
    close $f2
#   close $f3
#   close $f4

#    close $tchan2_

    exit 0
}
###########################################################
# The interface between TFRC Receive (of ACKS)            #
# and the adaptive rate controller of the VBR application #
###########################################################
Agent/TFRC instproc tfrc_ra {bytes_per_sec backlog} {
    global vbr ns numb_vbr
    #    $self instvar ns_ 
    $self instvar node_

    set now [$ns now]
    set node_id [$node_ id]
    #puts "In TFRC instproc. rate = $bytes_per_sec (B/s), node_id = $node_id"

    #    $ns at [expr $now] "$cbr1 set interval_ $interval"
    for {set i 0} {$i < $numb_vbr} {incr i} {
    	if {[$node_ id] == [expr $i*2+5]} {
	    if {[$node_ id] == 0} {
		puts "TCL: before vbr($i) TFRC_rateadapt rate=$bytes_per_sec node=$node_id"
	    }
	    $ns at [expr $now] "$vbr($i) TFRC_rateadapt $bytes_per_sec $node_id $backlog"
	    #puts "TCL: after vbr($i) TFRC_rateadapt rate=$bytes_per_sec node=$node_id"
    	} 
    }
}

#################################################
# Create Evalvid-RA traffic sources             #
#################################################
set rng2 [new RNG]
$rng2 seed 0
set xRate [new RandomVariable/Uniform]
$xRate use-rng $rng2
$xRate set min_ 0
$xRate set max_ 2.5e3
##
## VBR0 as VBR rate adaptiv traffic
##
# The followong file is used as ns-2 adapted (by Chih-Heng, Ke from Taiwan) version of 
# the Evalvid (J. Klaue) generated file above
# Sender trace file from mp4.exe containing frame type and size
puts "Evalvid-RA: Start making GOP and Frame trace files for $q_variants Rate variants"
for {set i 1} {$i <= $q_variants} {incr i} {
    set original_file_name($i) st_akiyo_cif.yuv_Q[expr $i + 1].txt 
    set original_file_id($i) [open $original_file_name($i) r]
}
set trace_file_name video2.dat
set trace_file_id [open $trace_file_name w]
set trace_file [new vbrTracefile2]
$trace_file filename $trace_file_name
set frame_count 0
set last_time 0

# AL: toggle between multiple input files!
#set original_file_id $original_file_id(1)
set source_select 1


set frame_size_file frame_size.dat
set frame_size_file_id [open $frame_size_file w]
for {set i 1} {$i <= $q_variants} {incr i} {
    set frame_size($i) 0
}
set gop_size_file gop_size.dat
set gop_size_file_id [open $gop_size_file w]
for {set i 1} {$i <= $q_variants} {incr i} {
    set gop_size($i) 0
}
set gop_numb 0
# Convert ASCII sender file on frame size granularity to ns-2 adapted internal format
while {[eof $original_file_id(1)] == 0} {
    for {set i 1} {$i <= $q_variants} {incr i} {
	gets $original_file_id($i) current_line($i)
    }
    
    scan $current_line(1) "%d%s%d%s%s%s%d%s" no_ frametype_ length_ tmp1_ tmp2_ tmp3_ tmp4_ tmp5_

    #puts "$no_ $frametype_ $length_ $tmp1_ $tmp2_ $tmp3_ $tmp4_ $tmp5_"
        
    # 30 frames/sec. if one want to generate 25 frames/sec, one can use set time [expr 1000*1000/25]
    set tempStr "%.0f"
    set time [expr 1000 * 1000/$frames_per_second] 
#    puts $f5 [format $tempStr $time]
    set time [format $tempStr $time]
    

    if { $frametype_ == "I" } {
  	set type_v 1
	set time 0
    }	

    if { $frametype_ == "P" } {
  	set type_v 2
    }	

    if { $frametype_ == "B" } {
  	set type_v 3
    }	
    
    # Write to GOP size file after each H-frame found:
    if { $frametype_ == "H" } {
	set puts_string "$gop_numb"
	for {set i 1} {$i <= $q_variants} {incr i} {
	    set puts_string "$puts_string $gop_size($i)" 
	}
	puts $gop_size_file_id $puts_string
	set gop_numb [expr $gop_numb + 1]
  	set type_v 0 ;# Must have different type than I-frame so that the LB(r,b) algorithm finds it!
    }	
# Write to frame_size.dat:
    set puts_string "$no_"
    for {set i 1} {$i <= $q_variants} {incr i} {
	set puts_string "$puts_string $gop_size($i)" 
    }
    puts $frame_size_file_id $puts_string

    for {set i 1} {$i <= $q_variants} {incr i} {
	scan $current_line($i) "%d%s%d%s%s%s%d%s" no_ frametype_ length($i) tmp1_ tmp2_ tmp3_ tmp4_ tmp5_
	set gop_size($i) [expr $gop_size($i) + $length($i) ]
    }

# Write to video2.dat:
    set puts_string "$time $length_ $type_v $max_fragmented_size"
    for {set i 2} {$i <= $q_variants} {incr i} {
	set puts_string "$puts_string $length($i)"
    }
    puts  $trace_file_id $puts_string
    incr frame_count
 
}
puts "Evalvid-RA: #of frames written to GOP and Frame trace files: $frame_count"
#close $original_file_id
close $trace_file_id  ;# Note that this new trace file is closed for writing and 
# opened below for reading through being a new Tracefile in eraTraceFile2::setup()

#######################################################################
# Add Evalvid-RA RA-SVBR sources                                      #
#######################################################################
for {set i 0} {$i < $numb_vbr} {incr i} {
    #puts "In SVBR TFRC loop, i=$i" 
    set tfrc($i) [new Agent/TFRC]
    $ns attach-agent $n([expr $i*2+0]) $tfrc($i)
    if {$cbr_true == 0} {
	set vbr($i) [new Application/Traffic/eraVbrTrace]
    } else {
	set vbr($i) [new Application/Traffic/CBR_RA]
	$vbr($i) set size $mean_psize
    }
    $vbr($i) attach-agent $tfrc($i)
    
    $tfrc($i) set packetSize_ $packetSize ;# this is the MSS for the TFRC
    $tfrc($i) set TOS_field_ 1     ;# New 120905: tag ECF enabled sources! 0 is default.
#    if {$i == 0 || $i == 1} 
    if {$i < 15} {
	$tfrc($i) set_filename sd_be_$i ;# Connect a file name to TFRC source to write transmit trace data
    }

    $vbr($i) set running_ 0 
    set offRate [$xRate value]
    if {$cbr_true == 0} {
	$vbr($i) set r_ [expr $vbr_rate + $offRate]   ;# Set the rate instead of packet interval, 
	#$vbr($i) set r_ [expr $vbr_rate + 0.0]   ;# Set the rate instead of packet interval, 
	
	#$vbr($i) set r_ [expr $vbr_rate + $i*0.5e3]   ;# Set the rate instead of packet interval, 
	;# eraVbrTrace will calculate the interval.
	$vbr($i) attach-tracefile $trace_file
	#    $vbr($i) set b_ [expr $vbr_rate * 1.5] 
	$vbr($i) set b_ 1.5 
	$vbr($i) set q_ 8
	$vbr($i) set GoP_ 12
	$vbr($i) set fps_ $frames_per_second
    } else {
	$vbr($i) set rate_ [expr $cbr_rate + 0]   ;# Set the max rate instead of 
    }

    $tfrc($i) set class_ 4
    
    set sink($i) [new Agent/TFRCSink] 
    
    $ns attach-agent $n([expr $i*2+1])  $sink($i)
    $ns connect $tfrc($i) $sink($i)
    if {$i == 0} {
	$sink($i) set_trace_filename rd_be_$i ;# Connect a file name to TFRC sink to 
	# write receivce trace data
    }
	
}

############################
## TCP sources
############################
for {set i 0} {$i < [expr $numb_ftp + $numb_web]} {incr i} {
    #set tcp [new Agent/TCP/Sack1]
    #puts "In TCP loop, i=$i" 
    set tcp($i) [new Agent/TCP/Newreno]
    $tcp($i) set class_ 2
    $tcp($i) set window_ 1000            ;# default is 20
    $tcp($i) set maxcwnd_ 1000
    $tcp($i) set packetSize_ 1460
    $tcp($i) set minrto_ 0.005
    $tcp($i) set maxrto_ 2
    $tcp($i) set backoff_ 0

    ### ECN ON/OFF ###
    $tcp($i) set ecn_ false

    #set sink [new Agent/TCPSink/Sack1]
    set sinktcp($i) [new Agent/TCPSink]
    $sinktcp($i) set window_ 64            ;# advertized window (?) default is 20
    $ns attach-agent $n([expr $i*2+0+2*$numb_vbr]) $tcp($i)
    $ns attach-agent $n([expr $i*2+1+2*$numb_vbr]) $sinktcp($i)
    $ns connect $tcp($i) $sinktcp($i)
    
    set ftp($i) [new Application/FTP]
    $ftp($i) attach-agent $tcp($i)
    $ftp($i) set packetSize_ 1420
}

##
## Trace of cwnd of selected TCP source
##
if {$numb_ftp > 2} {
    set tchan2_ [open t_cwnd.tr w]
    $tcp(0) trace cwnd_
    $tcp(0) attach $tchan2_ 
    $tcp(0) set trace_all_oneline_ 1
    $tcp(0) set tracevar_ 1

    set tchan3_ [open t_cwnd3.tr w]
    #$tcp trace cwnd_ || $tcp tracevar cwnd_
    $tcp(1) trace cwnd_
    $tcp(1) attach $tchan3_ 
    $tcp(1) set trace_all_oneline_ 1
    $tcp(1) set tracevar_ 1

    set tchan4_ [open t_cwnd4.tr w]
    #$tcp trace cwnd_ || $tcp tracevar cwnd_
    $tcp([expr $i-2]) trace cwnd_
    $tcp([expr $i-2]) attach $tchan4_ 
    $tcp([expr $i-2]) set trace_all_oneline_ 1
    $tcp([expr $i-2]) set tracevar_ 1
}
########################################################################################
## Create ns-2 scheduler
########################################################################################
set rng [new RNG]
#$rng seed 20
set ftpStart [new RandomVariable/Uniform]
$ftpStart use-rng $rng
$ftpStart set min_ 0.5
$ftpStart set max_ 2.0

########################################
## FTP/TCP traffic setup
########################################
for {set i 0} {$i < $numb_ftp} {incr i} {
    set startTime [$ftpStart value]
    #puts "startTime=$startTime"
    $ns at [expr $startTime] "$ftp($i) start"
    $ns at [expr $simtime - 2.0] "$ftp($i) stop"
}

########################################
## WEB traffic setup
########################################
set rngW [new RNG]
#$rngW seed 10
set interPage [new RandomVariable/Exponential]
$interPage use-rng $rngW
$interPage set avg_ 10
set pageSize [new RandomVariable/ParetoII]
$pageSize use-rng $rngW
$pageSize set avg_ 30
$pageSize set shape_ 1.35
## Web-traffic sources start
## Note that the start time for next burst may start before last has ended.
for {set i 0} {$i < $numb_web} {incr i} {
    set acctime 0 
    while {$acctime < $simtime} {
	$interPage value
	set iat_s [$interPage value]
	#puts "n.e.d.-variable $iat_s"
	set acctime [expr $acctime + $iat_s]
	#puts "acctime=$acctime, i=$i"
	$pageSize value
	set numpkt_s [expr ceil([$pageSize value])]
	#puts "pareto-variable $numpkt_s"	
	$ns at [expr $acctime+5.0] "$ftp([expr $i+$numb_ftp]) producemore $numpkt_s "
    }
}

########################################
## Evalvid-RA RA-SVBR traffic setup
########################################
set rng3 [new RNG]
#$rng3 seed 30
set vbrStart [new RandomVariable/Uniform]
$vbrStart use-rng $rng3
$vbrStart set min_ 0.040
$vbrStart set max_ 16.000
for {set i 0} {$i < $numb_vbr} {incr i} {
    set startTime [$vbrStart value]
    # The follow test starts OUR trace file first, so that it is recognized as main trace
    # and will thus start from beginning of file and stop simulation when finished!
    if {$i == 0} {
	$ns at 0.010 "$vbr($i) start"
    } else {
	$ns at $startTime "$vbr($i) start"
    }	
    $ns at $simtime   "$vbr($i) stop"
}

$ns at $simtime "finish"

$ns run
