<%--
  Created by IntelliJ IDEA.
  User: wanglt
  Date: 12-9-4
  Time: 下午3:15
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<div class="pageContent sortDrag" selector="h1" layoutH="42">
    <c:forEach items="${groupInfo}" var="group">
        <div class="panel" defH="${fn:length(group.storageList)*260}">
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
            <div>
                <c:forEach items="${group.storageList}" var="storage" varStatus="status">
                    <div class="dilstorage" style="margin-bottom: 5px">
                        <div class="dilchart"></div>
                        <div>
                            <input type="hidden" value="${storage.ipAddr}" name="dilip" />
                            <input type="hidden" value="${storage.curStatus}" name="dilstatus" />
                        </div>
                    </div>
                </c:forEach>
            </div>
        </div>
    </c:forEach>
</div>
<script type="text/javascript">
     function drawNetTrafficChart(obj,title,data) {
         new Highcharts.Chart({
             chart: {
                 renderTo: obj,
                 type:'area',
                 height:250,
                 width: 1150
             },
             title: {
                 text: title,
                 style: {
                     fontSize:'13px'
                 }
             },
             xAxis: {
                 labels: {
                     formatter: function() {
                         return  Highcharts.dateFormat('%m-%d', this.value);
                     }
                 }
             },
             tooltip: {
                 xDateFormat: '%Y-%m-%d %H:%M',
                 formatter: function() {
                     return '预计到'+ Highcharts.dateFormat('%Y-%m-%d %H:%M', this.x)+"将扩充到"+this.y+"G";
                 } ,
                 shared: true
             },
             yAxis: {
                 title: {
                     text: '扩充容量 (GB)'
                 }
             },
             legend:{
                 align: 'right',
                 layout:'vertical'
             },
             series:data
         });
      }
     function searchDilatation(){
         $(".dilstorage").each(function(){
             var toRender = $(this);
             var ip = toRender.find('input[name=dilip]').val();
             var status= toRender.find('input[name=dilstatus]').val();
             var title = ip +" "+toRender.find('input[name=dilstatus]').val()+"扩充容量形势图";
             if (ip=='10.1.216.108'){
                 title = "腾讯云：广州节点 "+toRender.find('input[name=dilstatus]').val()+"扩充容量形势图";
             }else if(ip == '10.1.216.113'){
                 title = "Microsoft Azure：中国东部数据中心 "+toRender.find('input[name=dilstatus]').val()+"扩充容量形势图";
             }else {
                 title = "阿里云：杭州节点 "+toRender.find('input[name=dilstatus]').val()+"扩充容量形势图";
             }
             var params = {ip:ip };
                  $.getJSON('forecast/getDilatation.shtml',params,function(data){
                         drawNetTrafficChart(toRender.find('.dilchart')[0],title,data);
                  });
         });
     };
     searchDilatation();
    </script>