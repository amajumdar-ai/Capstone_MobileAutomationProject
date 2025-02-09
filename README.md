Run this script to execute the test in multi devices- for device in $(adb devices | awk 'NR>1 && $2=="device" {print $1}'); do 
    echo "Applying multi-window settings on device: $device"
    adb -s $device shell settings put global block_resizable_multi_window false
done

After running the above command run this to execute the tests- mvn clean test -Dsurefire.suiteXmlFiles=testng.xml
