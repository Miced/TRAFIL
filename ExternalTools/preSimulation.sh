#!/bin/bash
source $1
cd VideoSimulationOutputs
for ((  counter=$outQualityStart ;  counter<=$outQualityEnd;  counter++  ))
do
    source $2
    wine "$ffmpegPath" -s $inVideoSize -r $inFrameRate -i "$videoPath" -vcodec $outVideoCodec  -g $outGOP  -sc_threshold 20000 -qscale $counter -s $outVideoSize -r $outFrameRate -y $ffmpegVideoNameOutput.m4v
    wine "$mp4Path" -send $mp4IpAddr $mp4Port $mp4FrameRate $mp4MTU $ffmpegVideoNameOutput.m4v > $mp4OutputFileName.txt
done

ns "$tclScript"
