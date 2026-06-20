& 'C:/Program Files/Javas/JDK21\bin\jpackage' `
    --verbose `
    --input D:/9b1d_8c6d_9dac_9dac/Documents/Codes/SofaAway/out/artifacts/SofaAway `
    --main-jar SofaAway.jar `
    --name SofaAway `
    --dest D:/9b1d_8c6d_9dac_9dac/Documents/Codes/SofaAway/build `
    --app-version "11.45.14" `
    --copyright None `
    --description "A moving sofa on your screen. 一个会在你屏幕上移动的沙发。" `
    --vendor None `
    --main-class cn.yuang2714.sofaaway.SofaAway `
    --type app-image `
    --resource-dir D:\9b1d_8c6d_9dac_9dac\Documents\Codes\SofaAway\build\resources

Remove-Item `
    -Path "\\?\D:\9b1d_8c6d_9dac_9dac\Documents\Codes\SofaAway\build\SofaAway\app\SofaAway\app" `
    -Recurse `
    -Force