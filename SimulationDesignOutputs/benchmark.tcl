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
create-god 3

# configure node

        $ns node-config -adhocRouting DSDV \
			 -llType LL \
			 -macType Mac/802_11 \
			 -ifqType Queue/DropTail/PriQueue \
			 -ifqLen  \
			 -antType Antenna/OmniAntenna \
			 -propType Propagation/TwoRayGround \
			 -phyType Phy/WirelessPhy \
			 -channelType Channel/WirelessChannel \
			 -topoInstance $topo \
			 -addressType flat \
			 -energyModel EnergyModel \
			 -wiredRouting OFF \
			 -mobileIP OFF \
			 -agentTrace ON \
			 -routerTrace ON \
			 -macTrace OFF \
			 -phyTrace OFF \
			 -movementTrace OFF \
			 -eotTrace OFF 

#Create nodes
set n0 [$ns node] #{TRAFIL} xpos=146.0 ypos=146.0 wired
set n1 [$ns node] #{TRAFIL} xpos=479.0 ypos=479.0 wired
set n2 [$ns node] #{TRAFIL} xpos=459.0 ypos=459.0 wired
set n3 [$ns node] #{TRAFIL} xpos=102.0 ypos=102.0 wired
set n4 [$ns node] #{TRAFIL} xpos=90.0 ypos=90.0 wireless
$n4 random-motion 0
set n5 [$ns node] #{TRAFIL} xpos=436.0 ypos=436.0 wireless
$n5 random-motion 0
set n6 [$ns node] #{TRAFIL} xpos=739.0 ypos=739.0 wireless
$n6 random-motion 0

#Create links between the nodes
$ns duplex-link $n0 $n1 1Mb 10ms DropTail
$ns duplex-link $n2 $n3 1Mb 10ms DropTail
$ns duplex-link $n2 $n1 1Mb 10ms DropTail

#Create agent tcp7 and attach them to node n0
set tcp7 [new Agent/TCP]
$ns attach-agent $n0 $tcp7

#Create traffic sources and attach them to agent tcp7
set cbr0 [new Application/Traffic/CBR]
$cbr0 set packetSize_ 500
$cbr0 set rate_ 0.5
$cbr0 set random_ 1
$cbr0 set packetSize_ 500
$cbr0 attach-agent $tcp7

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

#Create agent null4 and attach them to node n4
set null4 [new Agent/Null]
$ns attach-agent $n4 $null4

#Create agent null5 and attach them to node n5
set null5 [new Agent/Null]
$ns attach-agent $n5 $null5

#Create agent null6 and attach them to node n6
set null6 [new Agent/Null]
$ns attach-agent $n6 $null6

#Connect the traffic source with the traffic sink
$ns connect $tcp7 $null3

#Call the finish procedure after  seconds of simulation time
$ns at 4.0 "finish"

#Run the simulation
$ns run