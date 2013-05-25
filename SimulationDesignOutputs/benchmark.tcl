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

$topo load_flatgrid 100 300
# configure node
        $ns node-config -adhocRouting DSDV \
			 -llType LL \
			 -macType Mac/802_11 \
			 -ifqType Queue/DropTail/PriQueue \
			 -ifqLen 50 \
			 -antType Antenna/OmniAntenna \
			 -propType Propagation/TwoRayGround \
			 -phyType Phy/WirelessPhy \
			 -channelType Channel/WirelessChannel \
			 -addressType flat \
			 -energyModel EnergyModel \
			 -wiredRouting OFF \
			 -mobileIP OFF \
			 -agentTrace ON \
			 -routerTrace ON \
			 -macTrace OFF \
			 -phyTrace OFF \
			 -movementTrace OFF \
			 -eotTrace ON \
			 -topoInstance $topo

#Create nodes
set n0 [$ns node] #{TRAFIL} xpos=118.0 ypos=82.0 wired
set n1 [$ns node] #{TRAFIL} xpos=412.0 ypos=84.0 wired
set n2 [$ns node] #{TRAFIL} xpos=398.0 ypos=253.0 wired
set n3 [$ns node] #{TRAFIL} xpos=137.0 ypos=238.0 wired
set n4 [$ns node] #{TRAFIL} xpos=627.0 ypos=80.0 wireless
$n4 random-motion 0
set n5 [$ns node] #{TRAFIL} xpos=664.0 ypos=220.0 wireless
$n5 random-motion 0

#Create links between the nodes
$ns duplex-link $n0 $n1 1Mb 10ms DropTail #{TRAFIL} link
$ns duplex-link $n1 $n2 1Mb 10ms DropTail #{TRAFIL} link
$ns duplex-link $n2 $n3 1Mb 10ms DropTail #{TRAFIL} link

#Create agent tcp4 and attach them to node n0
set tcp4 [new Agent/TCP]
$ns attach-agent $n0 $tcp4

#Create traffic sources and attach them to agent tcp4
set cbr0 [new Application/Traffic/CBR]
$cbr0 set packetSize_ 400
$cbr0 set interval_ 0.05
$cbr0 set packetSize_ 200
$cbr0 attach-agent $tcp4

#Schedule events for the cbr0 source
$ns at 0.5 "$cbr0 start"
$ns at 2.0 "$cbr0 stop"

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
set cbr4 [new Application/Traffic/CBR]
$cbr4 set packetSize_ 200
$cbr4 set interval_ 0.3
$cbr4 attach-agent $udp7

#Schedule events for the cbr4 source
$ns at 0.5 "$cbr4 start"
$ns at 3.0 "$cbr4 stop"

#Create agent null8 and attach them to node n5
set null8 [new Agent/Null]
$ns attach-agent $n5 $null8

#Connect the traffic source with the traffic sink
$ns connect $tcp4 $null1
$ns connect $udp7 $null8

#Call the finish procedure after  seconds of simulation time
$ns at 4.0 "finish"

#Run the simulation
$ns run