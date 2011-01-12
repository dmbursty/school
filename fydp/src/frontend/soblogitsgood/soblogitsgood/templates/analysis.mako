<%inherit file="/sbig.mako" />
<%namespace name="helper" file="/helper.mako" />

<%def name="more_head_tags()">
  <script type="text/javascript" 
    src="http://ajax.googleapis.com/ajax/libs/jquery/1.4.2/jquery.min.js">
  </script>
  <script type="text/javascript" src="scripts/highcharts.js"></script>

<script type="text/javascript">
var piechart;
$(document).ready(function() {
  piechart = new Highcharts.Chart({
    chart: {
      renderTo: 'pie',
      margin: [0,10,0,0]
    },
    title: {
      text: null,
    },
    plotArea: {
      shadow: null,
      borderWidth: null,
      backgroundColor: null
    },
    tooltip: {
      formatter: function() {
        return '<b>'+ this.point.name +'</b>: '+ this.y +' results';
      }
    },
    plotOptions: {
      pie: {
        borderColor: '#000000',
        cursor: 'pointer',
        dataLabels: {
          enabled: true,
          formatter: function() {
            if (this.y > 5) return this.point.name;
          },
          color: 'white',
          style: {
            font: '13px Trebuchet MS, Verdana, sans-serif'
          }
        }
      }
    },
    legend: {
      enabled: true,
      layout: 'vertical',
      style: {
        left: 'auto',
        bottom: 'auto',
        right: '0px',
        top: '0px'
      }
    },
    series: [{
      type: 'pie',
      name: 'Good vs. Bad',
      data: [
        {
          name: 'Bad',
          y: ${len(c.badResults)},
          color: '#F95353'
        },
        {
          name: 'Good',  
          sliced: true,
          y: ${len(c.goodResults)},
          color: '#49BD43'
        }
      ]
    }]
  });
});

var piechart2;
$(document).ready(function() {
  piechart = new Highcharts.Chart({
    chart: {
      renderTo: 'pie2',
      margin: [0,10,0,0]
    },
    title: {
      text: null,
    },
    plotArea: {
      shadow: null,
      borderWidth: null,
      backgroundColor: null
    },
    tooltip: {
      formatter: function() {
        return '<b>'+ this.point.name +'</b>: '+ this.y +' results';
      }
    },
    plotOptions: {
      pie: {
        borderColor: '#000000',
        cursor: 'pointer',
        dataLabels: {
          enabled: true,
          formatter: function() {
            if (this.y > 5) return this.point.name;
          },
          color: 'white',
          style: {
            font: '13px Trebuchet MS, Verdana, sans-serif'
          }
        }
      }
    },
    legend: {
      enabled: true,
      layout: 'vertical',
      style: {
        left: 'auto',
        bottom: 'auto',
        right: '0px',
        top: '0px'
      }
    },
    series: [{
      type: 'pie',
      name: 'Good vs. Bad',
      data: [
        {
          name: 'Bad',
          y: ${len(c.badResults)},
          color: '#F95353'
        },
        {
          name: 'Good',  
          sliced: true,
          y: ${len(c.goodResults)},
          color: '#49BD43'
        }
      ]
    }]
  });
});


var linechart;
$(document).ready(function() {
    linechart = new Highcharts.Chart({
        chart: {
            renderTo: 'line',
            defaultSeriesType: 'line',
            marginRight: 30,
            marginBottom: 35
        },
        title: {
            text: 'Sentiment Distribution',
            x: -20 //center
        },
        yAxis: {
            min: 0,
            title: {
                text: 'Number of Results'
            },
            plotLines: [{
                value: 0,
                width: 1,
                color: '#808080'
            }]
        },
        tooltip: {
            formatter: function() {
                    return this.x +': '+ this.y ;
            }
        },
        legend: {
            enabled: false,
            layout: 'vertical',
            align: 'right',
            verticalAlign: 'top',
            x: -10,
            y: 100,
            borderWidth: 0
        },
        ${helper.sort(c.results)}
    });
});
    
$(document).ready(function(){
  $("#overview_graphs").hide();
  $("#stats_graphs").hide();
  
  $("#overview_header").toggle(function(){
    $("#overview_graphs").slideDown();
    $("#overview_header").html("Overview [-]")
  }, function() {
    $("#overview_graphs").slideUp();
    $("#overview_header").html("Overview [+]")
  });

  $("#stats_header").toggle(function(){
    $("#stats_graphs").slideDown();
    $("#stats_header").html("Statistics [-]")
  }, function() {
    $("#stats_graphs").slideUp();
    $("#stats_header").html("Statistics [+]")
  });
});		
</script>
</%def>

<table>
<tr>
<td>
  <table>
    <tr>
      <td>
        <div id="pie" style="width: 300px; height: 200px; margin: 0 auto"></div>
      </td>
      <td class="tagcloud">
        ${helper.tagCloud(c.goodTerms, c.badTerms)}
      </td>
    </tr>
  </table>
  <p></p>
</td>
</tr>
<tr>
  <td id="overview_header" class="graphheader">
    Overview [+]
  </td>
</tr>
<tr>
  <td>
    <div id="overview_graphs">
    <div id="line" style="width: 700px; height: 400px; margin: 0 auto"></div>
    </div>
  </td>
</tr>
<tr>
  <td id="stats_header" class="graphheader">
    Statistics [+]
  </td>
</tr>
<tr>
  <td>
    <div id="stats_graphs">
    <div id="pie2" style="width: 700px; height: 400px; margin: 0 auto"></div>
    </div>
  </td>
</tr>
</table>
