function waitPools(pools, interval)
%等待输入的 pools 全部工作完成，顺便绘制进度条

if length(pools) == 1
    pools = {pools};
end
if nargin < 2
    interval = 0.5;
end

barLengh = 50;
hasBar = false;

totalNum = getTaskNumber(pools);
if totalNum <= 0
    return
end

while true
    % 获取剩余任务数
    remainNum = getTaskNumber(pools);
    % 计算百分比
    percent = (totalNum - remainNum) / totalNum * 100;
    OutStr = sprintf('%3.0f%%', percent);
    % 清空上一次输出的进度条
    if hasBar
    OutStr = [repmat(char(8), 1, (barLengh+9)) newline OutStr];
    end
    % 绘制进度条
    if percent >= 100
    OutStr = [OutStr '[' repmat('=', 1, barLengh+1) ']'];
    else
    OutStr = [OutStr '[' repmat('=', 1, round(percent*barLengh/100)) '>', repmat(' ', 1, barLengh-round(percent*barLengh/100)) ']'];
    end
    disp(OutStr);
    hasBar = true;
    % 没有剩余的任务，所有任务完成
    if remainNum <= 0
        break;
    end
    % 等待更新间隔
    pause(interval);
end

end


function TaskNum = getTaskNumber(pools)
TaskNum = 0;
for i = 1:length(pools)
    TaskNum = TaskNum + pools{i}.getTaskNumber();
end
end

