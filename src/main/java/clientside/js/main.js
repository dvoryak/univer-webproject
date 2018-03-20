$(document).ready(function () {
    $("#send-btn").click(function () {
        var range = $("#data").val();

        if (checkData(range)) {
            $("#data-bl").addClass("close");
            $("#result-bl").removeClass("close");

            $.ajax({
                url: "http://localhost:8080/go",
                type: "get",
                data: {
                    data: range
                },
                success: function (resp) {
                    console.log(resp);
                    var trend = "T= " + parseFloat(resp.linearTrend[1]).toFixed(3)
                        + " + " + parseFloat(resp.linearTrend[0]).toFixed(3) + "t";
                    document.querySelector("#linear-trend").innerText = trend;
                    setTable($("#range-table"), resp.range, true);
                    setTable($("#season-table"), resp.seasonComponent);
                    setTable($("#forecast-table"), resp.forecast, generateHeaderForecast(resp.range.length));
                    drawGraphic(resp);

                   //
                },

                error: function () {
                    alert("err");
                }
            });
        } else {
            //TODO
        }

    });

    function generateHeaderForecast(size) {
        var arr = [];
        for (var i = size; i < size + 4; i++) {
            arr.push("F" + (i + 1));
        }
        return arr;
    }


    function drawGraphic(resp) {
        google.charts.load('current', {'packages':['corechart']});
        google.charts.setOnLoadCallback(drawChart);

        function drawChart(data) {
            var data = new google.visualization.DataTable();
            data.addColumn('number', 'Periods');
            data.addColumn('number', 'Range');
            data.addColumn('number', 'Model');
            data.addRows(mergeArray([generateArray(1,resp.range.length),resp.range,resp.forecastRange]));

            var options = {
                title: 'Forecast Graphic',
                curveType: 'function',
                legend: { position: 'bottom' }
            };

            var chart = new google.visualization.LineChart(document.getElementById('graphic'));

            chart.draw(data, options);
        }
    }

    function mergeArray(arr) {
        var out = [];
        for (var i = 0; i < arr[0].length; i++) {
            var tmp = [];
            for (var j = 0; j < arr.length; j++) {
                tmp.push(arr[j][i]);
            }
            out.push(tmp);
        }
        return out;
    }

    function generateArray(from, to) {
        if(!from) {
            from = 0;
        }

        var out = [];

        for (var i = from; i <= to; i++) {
            out.push(i);
        }

        return out;
    }

    function checkData(data) {
        /*console.log(data);
        var pattern = /\n\t|,| /;
        var arr = data.split(pattern);

        for (var i in arr) {
            var res = parseFloat(arr[i]);
            if(isNaN(res)) {
                return false;
            }
        }*/

        return true;
    }

    function setTable(table, data, header) {
        //console.log(data);
        if (header) {
            if (Array.isArray(header)) {
                for (var i = 0; i < header.length; i++) {
                    var th = $("<th>" + (header[i]) + "</th>");
                    table.append(th);
                }
            } else {
                for (var i = 0; i < data.length; i++) {
                    var th = $("<th>" + (i + 1) + "</th>");
                    table.append(th);
                }
            }
        }

        var tr = $("<tr></tr>");
        table.append(tr);

        for (var i = 0; i < data.length; i++) {
            var value = parseInt(data[i]);

            if (data[i].toString().includes(".")) {
                value = parseFloat(data[i]).toFixed(2);
            }

            var td = $("<td>" + value + "</td>");
            tr.append(td);
        }
    }


});
