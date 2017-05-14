<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib prefix="s" uri="http://java.sun.com/jsp/jstl/core" %>
<div layoutH="0">
    <c:forEach items="${groups}" var="group" varStatus="status">
    <div class="panel">
        <%--<h1>云供应商平台:${group.groupName}</h1>--%>
            <c:choose>
                <c:when test="${group.groupName=='group1'}">
                    <h1>云数据中心:腾讯云</h1>
                </c:when>
                <c:when test="${group.groupName=='group2'}">
                    <h1>云数据中心:Microsoft Azure</h1>
                </c:when>
                <c:when test="${group.groupName=='group3'}">
                    <h1>云数据中心:阿里云</h1>
                </c:when>
                <c:otherwise>
                    <h1>云数据中心:Amazon S3</h1>
                </c:otherwise>
            </c:choose>

        <c:forEach items="${group.storageList}" var="storageForForecast">
            <div class="forecastAreaYdm">
                <input type="hidden" value="${storageForForecast.ipAddr}" name="forForecastIp"/>
                <input type="hidden" value="${storageForForecast.curStatus}" name="statusYdm"/>

                <div class="bottchart"></div>
            </div>
        </c:forEach>
        </c:forEach>
    </div>

</div>
<script type="text/javascript">

    function drawAreaForForecastByYdm(id, data, ip) {
        var chart = new Highcharts.Chart({
            chart:{
                renderTo:id,
                type:'area',
                height:250,
                width:1100
            },
            title:{
                text:ip + " 剩余容量走势图"
            },
            xAxis: {
                labels: {
                    formatter: function() {
                        return  Highcharts.dateFormat('%m-%d', this.value);
                    }
                }
            },
            yAxis:{

                title: {
                    text: '剩余容量 (GB)'
                }
            },
            tooltip:{
                xDateFormat: '%Y-%m-%d %H:%M',
                formatter: function() {
                    return '预计到'+ Highcharts.dateFormat('%Y-%m-%d %H:%M', this.x)+"剩余"+this.y+"GB";
                } ,
                shared: true
            },
            credits:{
                enabled:false
            },
            series:data
        });
    }
    $("div.forecastAreaYdm").each(function () {
        var toRender = $(this);
        var ip = toRender.find('input[name=forForecastIp]').val();
        var title;
        if(ip == '10.1.216.108'){
            title = "腾讯云：广州节点";
        }else if(ip == '10.1.216.113'){
            title = "Microsoft Azure:中国东部数据中心";
        }else {
            title = "阿里云：杭州节点";
        }
        var status= toRender.find('input[name=statusYdm]').val();
            $.getJSON('forecast/drawAreaAction.shtml', {ip:ip}, function (data) {
//                drawAreaForForecastByYdm(toRender.find('.bottchart')[0], data, ip+status);
                drawAreaForForecastByYdm(toRender.find('.bottchart')[0], data, title + status);
            })

    })
</script>