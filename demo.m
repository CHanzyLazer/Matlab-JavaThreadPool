%% load include
clear;
addpath('include');
javaaddpath('include');

%% code
pool1 = SystemThreadPool(4);
pool2 = SystemThreadPool(3);
for i=1:20; pool1.submitSystem(['powershell Start-Sleep -Seconds ' num2str(randi(10))]); end
for i=1:20; pool2.submitSystem(['powershell Start-Sleep -Seconds ' num2str(randi(10))]); end
waitPools({pool1, pool2});

pool1.shutdown();
pool2.shutdown();
%% unload include
clear;
javarmpath('include');
rmpath('include');
