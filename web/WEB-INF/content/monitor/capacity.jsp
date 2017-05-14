<%@ taglib prefix="c" uri="http://java.sun.com/jstl/core_rt"%>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<div class="pageContent sortDrag" selector="h1" layoutH="42">
	<c:forEach items="${groupInfo}" var="group">
		<div class="panel" defH="200">
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
			<div id="${group.groupName}">
				<c:forEach items="${group.storageList}" var="storage"
					varStatus="status">
					<div  class="storage"
						style="float: left; margin-right: 5px">
						<input type="hidden" value="${storage.freeMB}" name="freeMB" /> <input
							type="hidden" value="${storage.totalMB}" name="totalMB" /> <input
							type="hidden" value="${storage.ipAddr}" name="ip" />
					</div>
				</c:forEach>
			</div>
		</div>
	</c:forEach>
</div>
<script type="text/javascript">
    function createChart(id,totalMB,freeMB,name,ip){
        chart = new Highcharts.Chart({
            chart: {
                renderTo: id,
                plotBackgroundColor: null,
                plotBorderWidth: null,
                plotShadow: false,
                height:200,
                width:280,events:{
                    click:function(){
                        navTab.openTab("toStorageInfo","monitor/storageInfo.shtml?ip="+ip,{title:'容量历史'})
                    }
                }
            },
            title: {
                text: name,
                style: {
                    fontSize:'13px'
                }
            },
            tooltip: {
                formatter: function() {
                    return '<b>'+ this.point.name +'</b> '+ this.percentage.toFixed(2) +' %';
                }
            },
            plotOptions: {
                pie: {
                    allowPointSelect: true,
                    cursor: 'pointer',
                    dataLabels: {
                        enabled: true,
                        color: '#000000',
                        connectorColor: '#000000',
                        formatter: function() {
                            return this.percentage.toFixed(2) +' %';
                        }
                    }
                }
            },
            series: [{
                type: 'pie',
                name: 'Browser share',
                data: [{
                    name: '已使用:'+((totalMB-freeMB)/1024).toFixed(2)+"G",
                    y: 1-freeMB/totalMB,
                    sliced: true,
                    selected: true,
                    color:'red'
                },{
                    name:'未使用:'+((freeMB)/1024).toFixed(2)+"G",
                    y:freeMB/totalMB,
                    color:'green'
                }]
            }],
            credits:{
                href:'javascript:void(0)'
                /*text:'vivame.cn'*/
            }
        });
    }

    $("div.storage").each(function(){
        var toRender=$(this);
        var freeMB = $(this).find('input[name=freeMB]').val();
        var totalMB = $(this).find('input[name=totalMB]').val();
        var ip = $(this).find('input[name=ip]').val();
        freeMB = parseInt(freeMB)+313892;
        totalMB = 512000;
        if (ip=='10.1.216.108'){
            ip='广州';
        }else if(ip=='10.1.216.113'){
            ip='中国东部数据中心';
        }else{
            ip='杭州';
        }
        var name = '节点位置:'+ip+' 容量:'+ (totalMB/1024).toFixed(2)+'G';
        createChart(toRender[0],totalMB,freeMB,name,ip);
    });

</script>
