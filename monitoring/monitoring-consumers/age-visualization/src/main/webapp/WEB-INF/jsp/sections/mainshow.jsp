<%@page contentType="text/html"%>
<%@page pageEncoding="UTF-8"%>
<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles" %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
   "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<link rel="stylesheet" href="<%=request.getContextPath()%>/style.css" />
<script type="text/javascript"
	src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js"></script>
<script src="http://code.highcharts.com/stock/highstock.js"></script>
<script src="http://code.highcharts.com/stock/modules/exporting.js"></script>
<script type="text/javascript">
jQuery.noConflict();
</script>

<script type="text/javascript">

(function($){ // encapsulate jQuery

	$(function() {
		var seriesList = ${seriesList};
		var chartId = ${chartId};
		
		var chartOptions = {
				chart : {
					height : 540,
					zoomType:'y',
					events : {
						load : function() {
							// set up the updating of the chart each second
							
							var series = new Array();
							for(var i = 0; i<seriesList.length; i++){
								series[i] = this.series[i];
							}
							
							var lastDateMap = new Object();
							for(var i = 0; i<seriesList.length; i++){
								lastDateMap[i] = 0;
							}
							
		                    var chart = this;
							setInterval(
									function() {
						        $.ajax({    
						            url : '/age/update',
						            type : 'GET',
						            async: true,
						            data : (
						            		{chartId : chartId,
						            		lastDateMap : lastDateMap}
						            		),              
						            success : function(data) {
									for(var seriesNum = 0; seriesNum < seriesList.length; seriesNum++){
						            	var currentSeries = data[seriesNum];
						            	console.log(currentSeries);
										for(var i = 0; i < currentSeries.length; i++){
											var currentVisualData = currentSeries[i];
											var x = currentVisualData.timestamp,
											y = currentVisualData.data;
											console.log(x + " y=" + y);
											series[seriesNum].addPoint([x, y]);
											lastDateMap[seriesNum] = x;
										}
									}
				                    chart.redraw();	

						            },
						            error: function (jqXHR, textStatus, errorThrown) {
						                alert(jqXHR + " : " + textStatus + " : " + errorThrown);
						            }
						        });
						    }
									, 1000);
						}
					}
				},
				
			    legend: {
			    	enabled: true,
			    	align: 'center',
		        	//backgroundColor: '#FCFFC5',
		        	borderColor: 'black',
		        	borderWidth: 1,
			    	layout: 'horizontal',
			    	verticalAlign: 'bottom',
		            //floating: true,
			    	shadow: true
			    },
				
			    plotOptions: {
			    	series: {
			    		marker: {
			    			enabled: true	
			    		}
			    	}
			    },
			    
			    rangeSelector: {
			    	enabled: false
			    },
				
			    tooltip: {
			        xDateFormat: '%M:%S.%L'
			    },
			    
			    xAxis: {
		        	type: 'datetime',
		        	dateTimeLabelFormats: {
		        		millisecond: '%M:%S.%L',
		        		second: '%M:%S'
					}
		    	},

				navigator: {
					xAxis: {
						dateTimeLabelFormats: {
				    		millisecond: '%M:%S.%L',
				        	second: '%M:%S'
				   		}
				    }
				},
				
				exporting: {
					enabled: true
				},
				
				series : seriesList
				
			};
		
		Highcharts.setOptions({
			global : {
				useUTC : false
			}
		});
		
		// Create the chart
		
		$('#container').highcharts('StockChart', chartOptions);
		
	});

})(jQuery);

</script>

</head>
<body style="margin: 0; height: 100%;">
		<div id="topId">
		<div id="headerId">
			<tiles:insertAttribute name="header" />
		</div>
		<div id="bodyId">
			<tiles:insertAttribute name="body" />
		</div>
	</div>
</body>
</html>