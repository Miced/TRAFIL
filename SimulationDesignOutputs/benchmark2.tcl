# ======================================================================
# Main Program
# ======================================================================
#Create a simulator object
set ns [new Simulator]
set tracefd     [open benchmark2.tr w]
$ns trace-all $tracefd

#Define a 'finish' procedure
proc finish {} {
        global ns
        $ns flush-trace
	#Close the trace file
        exit 0
}

#Create nodes
set n0 [$ns node] #{TRAFIL} xpos=125.0 ypos=100.0 wired
set n1 [$ns node] #{TRAFIL} xpos=325.0 ypos=86.0 wired
set n2 [$ns node] #{TRAFIL} xpos=323.0 ypos=217.0 wired
set n3 [$ns node] #{TRAFIL} xpos=144.0 ypos=227.0 wired

#Create links between the nodes
$ns duplex-link $n0 $n1 1Mb 10ms DropTail #{TRAFIL} link
$ns duplex-link $n1 $n2 1Mb 10ms DropTail #{TRAFIL} link
$ns duplex-link $n2 $n3 1Mb 10ms DropTail #{TRAFIL} link

#Create agent tcp4 and attach them to node n0
set tcp4 [new Agent/TCP]
$ns attach-agent $n0 $tcp4

#Create traffic sources and attach them to agent tcp4
set cbr0 [new Application/Traffic/CBR]
$cbr0 set packetSize_ 500
$cbr0 set interval_ 0.05
$cbr0 set random_ 1
$cbr0 set packetSize_ 500
$cbr0 attach-agent $tcp4

#Schedule events for the cbr0 source
$ns at 1.5 "$cbr0 start"
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

#Connect the traffic source with the traffic sink
$ns connect $tcp4 $null2

#Call the finish procedure after  seconds of simulation time
$ns at 4.0 "finish"

#Run the simulation
$ns run