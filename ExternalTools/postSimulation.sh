#!/bin/bash
cd VideoSimulationOutputs
source $1
source $2
que=_Q
wine "$et_raPath" $senderFile $receiverFile $mp4TraceFile $dataFile1 $ffmpegPre$videoName$ffmpegMiddle  $outQualityStart $outQualityEnd $dataFile2 $et_raVideoOutputFile.m4v 01
wine "$ffmpegPath" -r $postSimulationFrameRate -i $et_raVideoOutputFile.m4v -vcodec $postSimulationVideoCodec -y $ffmpegPostSimulationOutputVideoName.yuv
wine "$psnrPath" $psnrVideoWidth $psnrVideoHeight  420 "$videoPath" $ffmpegPostSimulationOutputVideoName.yuv > psnr_1.txt
