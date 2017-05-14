<%--
  Created by IntelliJ IDEA.
  User: Chen.H
  Date: 12-8-23
  Time: 上午11:19
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<script>

</script>
<h2 class="contentTitle">性能监控</h2>

<div class="pageContent sortDrag" selector="h1" layoutH="42">

    <c:forEach items="${groups}" var="groupb" varStatus="groupstatus">

        <div class="panel collapse" minH="100" defH="250">
                <%--<h1>${groupb.groupName}</h1>--%>
            <c:choose>
                <c:when test="${groupb.groupName=='group1'}">
                    <h1>云数据中心:腾讯云</h1>
                </c:when>
                <c:when test="${groupb.groupName=='group2'}">
                    <h1>云数据中心:Microsoft Azure</h1>
                </c:when>
                <c:when test="${groupb.groupName=='group3'}">
                    <h1>云数据中心:阿里云</h1>
                </c:when>
                <c:otherwise>
                    <h1>云数据中心:Amazon S3</h1>
                </c:otherwise>
            </c:choose>
            <div>
                <div id="_performance${groupb.groupName}" val="${groupb.groupName}" class="performancestorage"
                     style="width: 700px; margin-left: 17px; float: left; height: 240px; position: relative; z-index: 1"></div>

                <div class="pageContent">
                    <div class="tabs" currentIndex="0" eventType="click">
                        <div class="tabsHeader">
                            <div class="tabsHeaderContent">
                                <ul>
                                    <li><a href="javascript:;"><span>基本信息</span></a></li>
                                    <li><a href="javascript:;"><span>高级信息</span></a></li>

                                </ul>
                            </div>
                        </div>
                        <div class="tabsContent" style="height:200px;">
                            <div class="storagebaseinfo">


                                <c:forEach items="${groupb.storageList}" var="storage" varStatus="storagestatus">

                                    <fieldset style="background-color: #ffffff;">
                                        <c:choose>
                                            <c:when test="${storage.ipAddr=='10.1.216.108'}">
                                                <legend style="background-color: #ffffff">广州节点</legend>
                                                <p style="margin-top:10px;margin-left:10px;">
                                                    <b>cpu : </b>${storage.cpu+5.42}%<br>
                                                    <b>mem : </b><fmt:formatNumber type="percent" minFractionDigits="2" value="${(storage.mem+'6.35f')/100}" pattern="##.##%"></fmt:formatNumber><br>
                                                    <b>状态 : </b><font color="${storage.curStatus=='ACTIVE'?"green":"red"}">${storage.curStatus}</font>
                                                </p>
                                            </c:when>
                                            <c:when test="${storage.ipAddr=='10.1.216.113'}">
                                                <legend style="background-color: #ffffff">中国东部数据中心节点</legend>
                                                <p style="margin-top:10px;margin-left:10px;">
                                                    <b>cpu : </b><fmt:formatNumber type="percent" minFractionDigits="2" value="${(storage.cpu+5.27)/100}" pattern="##.##%"></fmt:formatNumber><br>
                                                    <b>mem : </b><fmt:formatNumber type="percent" minFractionDigits="2" value="${(storage.mem+'6.35f')/100}" pattern="##.##%"></fmt:formatNumber><br>
                                                    <b>状态 : </b><font color="${storage.curStatus=='ACTIVE'?"green":"red"}">${storage.curStatus}</font>
                                                </p>
                                            </c:when>
                                            <c:otherwise>
                                                <legend style="background-color: #ffffff">杭州节点</legend>
                                                <p style="margin-top:10px;margin-left:10px;">
                                                    <b>cpu : </b>${storage.cpu+5.42}%<br>
                                                    <b>mem : </b><fmt:formatNumber type="percent" minFractionDigits="2" value="${(storage.mem+'6.35f')/100}" pattern="##.##%"></fmt:formatNumber><br>
                                                    <b>状态 : </b><font color="${storage.curStatus=='ACTIVE'?"green":"red"}">${storage.curStatus}</font>
                                                </p>
                                            </c:otherwise>
                                        </c:choose>

                                        <p style="margin-top:10px;margin-left:10px;">
                                            <%--<b>ifTrunkServer : </b>${storage.ifTrunkServer}--%>
                                            <b>最后更新日期 : </b> <fmt:formatDate value="${storage.created}" pattern="yyyy-MM-dd HH:mm:ss"/>
                                        </p>
                                    </fieldset>
                                </c:forEach>
                            </div>
                            <div class="storagefielset">

                                <c:forEach items="${groupb.storageList}" var="storage" varStatus="storagestatus">

                                    <fieldset id="_${groupb.groupName}_${groupstatus.index}_${storagestatus.index}"
                                              style="background-color: #ffffff;">
                                        <input type="hidden" value="${storage.ipAddr}" name="ipAddr"/>
                                        <input type="hidden" value="${storage.mem}" name="mem" ip="${storage.ipAddr}"/>
                                        <legend style="background-color: #ffffff">${storage.ipAddr}</legend>
                                        <p style="margin-top:10px;margin-left:10px;"><b>version : </b>${storage.version}
                                            <b>硬盘总容量 : </b><span id="_total_${groupstatus.index}_${storagestatus.index}"
                                                                 value="${storage.totalMB}"></span> <b>硬盘剩余容量
                                                : </b><span id="_freeMB_${groupstatus.index}_${storagestatus.index}"
                                                            value="${storage.freeMB}"></span></p>
                                        <p style="margin-top:10px;margin-left:10px;"><b>upload priority
                                            : </b>${storage.uploadPriority} <b>joinTime : </b><fmt:formatDate
                                                value="${storage.joinTime}" pattern="yyyy-MM-dd HH:mm:ss"/> <b>upTime
                                            : </b><fmt:formatDate value="${storage.upTime}"
                                                                  pattern="yyyy-MM-dd HH:mm:ss"/></p>

                                        <p style="margin-top:10px;margin-left:10px;"><b>totalUploadCount
                                            : </b>${storage.totalUploadCount}  </p>
                                    </fieldset>
                                </c:forEach>
                            </div>
                        </div>
                        <div class="tabsFooter">
                            <div class="tabsFooterContent"></div>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </c:forEach>

</div>

<script type="text/javascript">

    function createPerformanceChart(obj, data) {
        new Highcharts.Chart({
            chart: {
                renderTo: obj,
                height: 240,
                width: 700,
                type: 'line'
            },
            title: {
                text: "",
                style: {
                    fontSize: '13px'
                }
            },
            xAxis: {
                type: 'datetime',
                dateTimeLabelFormats: {
                    day: '%e of %b'
                },
                minPadding: 0.01,
                maxPadding: 0.01
            },
            yAxis: {
                title: {
                    text: 'MEM和CPU使用率 (%)'
                },
                plotLines: [{
                    value: 0,
                    width: 20

                }]
            },
            tooltip: {  // 表示为鼠标放在报表图中数据点上显示的数据信息
                formatter: function () {
                    return '<b>' + '日期:' + '</b>'
                            + Highcharts.dateFormat('%Y-%m-%d %H:%M', this.x) + '<br/>'
                            + this.series.name + '</b>' + ': ' + this.y + '%';
                }
            },
            legend: {
                layout: 'vertical',
                align: 'left',
                floating: true,
                verticalAlign: 'top',
                x: 300,
                y: 50,
                borderWidth: 1
            },
            series: data
        });

    }
    ;

    $(".storagefielset > fieldset > p > span").each(function () {
        $("#" + this.id).html(($("#" + this.id).attr("value") / 1024).toFixed(2) + 'G');
    });
    var ipMap = new HashMap();

    $(".storagefielset > fieldset ").each(function () {
        var ip = $(this).find('input[name=ipAddr]').val();
        console.log(ip);
        ipMap.put(ip, ip);
    });

    $(".performancestorage").each(function () {
        console.log($(this).attr("val"));
        var toRender = $(this);
        $.getJSON('monitor/getPerformanceLine.shtml', {groupName: $(this).attr("val")}, function (data) {
            createPerformanceChart(toRender[0], data);
        });

    });

</script>