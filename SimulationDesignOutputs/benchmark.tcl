# ======================================================================
# Main Program
# ======================================================================
#Create a simulator object
set ns [new Simulator]
set tracefd     [open benchmark.tr w]
$ns trace-all $tracefd

#Define a 'finish' procedure
proc finish {} {
        global ns
        $ns flush-trace
	#Close the trace file
        exit 0
}

# set up topography object
set topo       [new Topography]

$topo load_flatgrid 500 500 
# configure node

        $ns_ node-config -adhocRouting DSDV \
			 -llType LL \
			 -macType Mac/802_11 \
			 -ifqType Queue/DropTail/PriQueue \
			 -ifqLen 50 \
			 -antType Antenna/OmniAntenna \
			 -propType Propagation/TwoRayGround \
			 -phyType Phy/WirelessPhy \
			 -channelType Channel/WirelessChannel \
			 -topoInstance $topo \
			 -addressType flat \
			 -energyModel EnergyModel \
			 -rxPower 133 \
			 -wiredRouting OFF \
			 -mobileIP OFF \
			 -agentTrace OFF \
			 -routerTrace OFF \
			 -macTrace ON \
			 -phyTrace OFF \
			 -movementTrace OFF \
			 -eotTrace ON 

#Create nodes
set n0 [$ns node] #{TRAFIL} xpos=76.0 ypos=54.0 wired
set n1 [$ns node] #{TRAFIL} xpos=479.0 ypos=80.0 wired
set n2 [$ns node] #{TRAFIL} xpos=616.0 ypos=277.0 wired
set n3 [$ns node] #{TRAFIL} xpos=202.0 ypos=333.0 wired
set n4 [$ns node] #{TRAFIL} xpos=115.0 ypos=159.0 wireless
$n4 random-motion 0
set n5 [$ns node] #{TRAFIL} xpos=676.0 ypos=87.0 wireless
$n5 random-motion 0

#Create links between the nodes
$ns duplex-link $n0 $n1 1Mb 10ms DropTail #{TRAFIL} link
$ns duplex-link $n1 $n2 1Mb 10ms DropTail #{TRAFIL} link
$ns duplex-link $n2 $n3 1Mb 10ms DropTail #{TRAFIL} link

#Create agent tcp6 and attach them to node n0
set tcp6 [new Agent/TCP]
$ns attach-agent $n0 $tcp6

#Create traffic sources and attach them to agent tcp6
set cbr0 [new Application/Traffic/CBR]
$cbr0 set packetSize_ 500
$cbr0 set interval_ 0.50
$cbr0 attach-agent $tcp6

#Schedule events for the cbr0 source
$ns at 0.5 "$cbr0 start"
$ns at 1.0 "$cbr0 stop"

#Create agent null1 and attach them to node n1
set null1 [new Agent/Null]
$ns attach-agent $n1 $null1

#Create agent null2 and attach them to node n2
set null2 [new Agent/Null]
$ns attach-agent $n2 $null2

#Create agent null3 and attach them to node n3
set null3 [new Agent/Null]
$ns attach-agent $n3 $null3

#Create agent udp7 and attach them to node n4
set udp7 [new Agent/UDP]
$ns attach-agent $n4 $udp7

#Create traffic sources and attach them to agent udp7
set ftp6 [new Application/FTP]
$ftp6 set maxpkts_ 50
#Create agent null5 and attach them to node n5
set null5 [new Agent/Null]
$ns attach-agent $n5 $null5

#Connect the traffic source with the traffic sink
$ns connect $tcp6 $null3
$ns connect $udp7 $null

#Call the finish procedure after  seconds of simulation time
$ns at 4.0 "finish"

#Run the simulation
$ns run