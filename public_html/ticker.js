/* MAKE SURE THIS IS LOADED AFTER SCRIPT.JS. */

//function for populating data in ticker.php
function ticker() {
        var symbol = getUrlParameter('s');
        var feedback = "";

        if (!symbol) {
                feedback = "No search query supplied, please use the search bar to find a stock symbol";
                console.log(feedback);
                $('#stats_go_here').html(feedback);
                $('#chart_placeholder').html(feedback);
                $('#company_name').html("No stock specified");
                $('#ticker_about').html("No stock specified");
                return;
        }

        $.ajax({
                        method: "GET",
                        crossDomain: true,
                        xhrFields: {
                                withCredentials: true
                        },
                        url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/symbol/" + symbol,
                })
                .done(function(data, textStatus, xhr) {
                        console.log('Ticker data retrieved: ' + data);

                        var name = data.company;
                        var symbol = data.symbol;
                        var graphData = data.todayData;
                        var yesterday = data.yesterdayClose;
                        var stats = data.statistics;
                        var about = data.about;
                        var articles = data.articles;

                        var current = data.currentPrice;
                        var priceChange = data.priceChange;
                        var percentChange = data.percentChange;

                        //fill in company Name and About
                        $("#company_name").html(name + ' (<span id="stockSymbol">' + symbol + '</span>)');
                        $("#ticker_about").html(about);

                        $('#currentPrice').html("$" + current);
                        $('#priceChange').html(priceChange);
                        $('#percentChange').html("" + (percentChange * 100) + '%');

                        if (percentChange > 0) { //color
                                $('#priceColor').css('color', 'green');
                                //$('#percentChange').attr('style', 'color:green');

                        } else if (percentChange < 0) {
                                $('#priceColor').css('color', 'red');
                                //$('#percentChange').attr('style', 'color:red');
                        }

                        var i, j;
                        var new_table = "<table class='table stock_tables'>";
                        new_table += "<tr><th>Statistics</th></tr>";

                        //dynamically generate tables based on server data
                        for (key in stats) {
                                new_table += "<tr>";
                                new_table += "<td>" + key + "</td>";
                                new_table += "<td>" + stats[key] + "</td>";

                                new_table += "</tr>";
                        }
                        new_table += "</table>";

                        //insert the table into the page
                        $("#stats_go_here").html(new_table);

                        setupNews(articles);

                        /*
                        var new_article;
                        for (i = 0; i < articles.length; i++) {
                        	new_article = $("#article_template").clone();
                        	$(new_article).removeClass("d-none");
                        	$(new_article).removeAttr("id");

                        	$(new_article).find(".artcile_img").html("<img height=50% width=50% src=" + articles[i].image + ">");
                        	$(new_article).find(".article_headline").html(articles[i].headline);
                        	$(new_article).find(".article_summary").html(articles[i].summary);
                        	$(new_article).attr("href", articles[i].url);
                        	$("#news_articles").append(new_article);
                        }

                        $("#article_page_number").html("1");
                        */

                        $('#chart_placeholder').remove();
                        graph(graphData, '1d', false, yesterday); //defaults to 1d

                })
                .fail(function(xhr, textStatus, errorThrown) {
                        var statusNum = xhr.status;
                        var feedback = "";

                        switch (statusNum) {
                                case 200: //not sure why this is considered a fail...
                                        feedback = "No results found for this ticker";
                                        break;
                                case 401:
                                        feedback = "search: 401 (token not found, or invalid)";
                                        //TODO: redirect to sign on page?
                                        break;
                                default:
                                        feedback = "The server returned an undefined response: Status code " + statusNum;
                                        break;
                        }
                        console.log(feedback);
                        $('#stats_go_here').html(feedback);
                        $('#chart_placeholder').html(feedback);
                });
}


//taken from best answer here: https://stackoverflow.com/questions/19491336/get-url-parameter-jquery-or-how-to-get-query-string-values-in-js
function getUrlParameter(sParam) {
        var sPageURL = window.location.search.substring(1),
                sURLVariables = sPageURL.split('&'),
                sParameterName,
                i;

        for (i = 0; i < sURLVariables.length; i++) {
                sParameterName = sURLVariables[i].split('=');

                if (sParameterName[0] === sParam) {
                        return sParameterName[1] === undefined ? true : decodeURIComponent(sParameterName[1]);
                }
        }
};


function generateData(tbl, labels, key) {
        var arr = [];
        var i, index, date;

        for (i = 0; i < tbl.length; i++) {
                date = moment(tbl[i][key[0]]);

                if ((index = labels.map(Number).indexOf(+moment(tbl[i][key[0]]))) != -1) {
                        arr[index] = Number(tbl[i][key[1]]);
                }
        }

        return arr;
}

function generateLabels(tbl, key) {
        var arr = [];

        for (var i = 0; i < tbl.length; i++) {
                arr.push(moment(tbl[i][key[0]]));
        }

        return arr;
}

function setOpenPrice(tbl, key) {
        var openIndex = 0;
        var earliestDate = moment().valueOf();

        for (var i = 0; i < tbl.length; i++) {
                if (moment(tbl[i][key[0]]).valueOf() < earliestDate) {
                        openIndex = i;
                        earliestDate = moment(tbl[i][key[0]]).valueOf();
                }
        }

        return Number(tbl[openIndex][key[1]]);
}

function chooseColor(tbl, key, currDate) {
        var lastPrice, lastDate, date, openPrice;
        lastDate = currDate.clone().year(1902);
        openPrice = setOpenPrice(tbl, key);

        for (var i = 0; i < tbl.length; i++) {
                date = moment(tbl[i][key[0]]);
                if (date.valueOf() >= lastDate.valueOf()) {
                        lastDate = date;
                        lastPrice = Number(tbl[i][key[1]]);
                }
        }

        if (openPrice >= lastPrice) {
                return 'rgba(244, 85, 49, 0.5)'; //red
        } else {
                return 'rgba(33, 206, 153, 0.5)'; //green
        }
}

function graph(graphData, hist = '1d', dash, yesterdayClose) { //pass in data.todayData from AJAX request
        var tmp = $('#myChart').clone();
		$('#myChart').remove();
		$('#chart-area').append(tmp);
		
		var ctx = document.getElementById('myChart').getContext('2d');

        var tbl = graphData;
        var key = [];
        key[0] = 'date';
        if (dash) {
                key[1] = 'value'
        } else if (hist == '1d') {
                key[1] = 'marketAverage';
        } else {
                key[1] = 'Close';
		key[0] = 'Date';
        }
        var currDate = moment();
        if (hist == '1d') {
                currDate = moment(tbl[0][key[0]]);
                yesterdayClose['date'] = currDate.clone().set({
                        'hours': 9,
                        'minutes': 0,
                        'seconds': 0,
                        'milliseconds': 0
                });
                tbl.push(yesterdayClose);
        }
        var graphColor = (tbl.length == 0) ? 'rgba(0,0,0,0.1)' : chooseColor(tbl, key, currDate);
        var labels = generateLabels(tbl, key);
        var xTimeUnit = 'day';
        var xMin = currDate.clone();
        var xMax = currDate.clone().subtract(1, 'days');
        switch (hist) {
                case '1d':
			xTimeUnit = 'minute';
                        xMin = currDate.clone().set({
                                'hours': 9,
                                'minutes': 0,
                                'seconds': 0,
                                'milliseconds': 0
                        });
                        xMax = currDate.clone().set({
                                'hours': 16,
                                'minutes': 0,
                                'seconds': 0,
                                'milliseconds': 0
                        });
                        break;
                case '1w':
                        xMin.subtract(7, 'days');
                        break;
                case '1m':
                        xMin.subtract(1, 'months');
                        break;
                case '3m':
                        xMin.subtract(3, 'months');
                        break;
                case '1y':
                        xMin.subtract(1, 'years');
                        break;
                case '5y':
                case 'max':
                        xMin = currDate.clone().subtract(5, 'years');
                        break;
		case 'dash':
			xTimeUnit = 'hour';
			xMin = moment(tbl[tbl.length - 1][key[0]]);
			xMax = moment(tbl[0][key[0]]);
			break;
                default:
                        break;
        }

        var data = {
                labels: labels,
                datasets: [{
                        data: generateData(tbl, labels, key),
                        hidden: false,
                        backgroundColor: graphColor,
                        pointBorderColor: 'rgba(0, 0, 0, 0)',
                        pointBackgroundColor: 'rgba(0, 0, 0, 0)',
                        pointHoverBackgroundColor: graphColor
                }]
        };

        var options = {
                maintainAspectRatio: false,
                spanGaps: false,
                legend: {
                        display: false
                },
                elements: {
                        line: {
                                tension: 0,
                                borderColor: graphColor
                        }
                },
                scales: {
                        xAxes: [{
                                type: 'time',
                                distribution: 'series',
                                time: {
                                        unit: xTimeUnit,
                                        min: xMin,
                                        max: xMax
                                },
                                bounds: 'ticks',
                                gridLines: {
                                        color: 'rgba(0, 0, 0, 0.01)'
                                },
                                ticks: {
                                        autoSkip: true
                                }
                        }]
                },
                tooltips: {
                        intersect: false,
                        mode: 'x',
                }
        };

        var chart = new Chart(ctx, {
                type: 'line',
                data: data,
                options: options
        });
}

//valid history params: 1w, 1m, 3m, 1y, 5y, max
function getGraphGranular(history) {
        var symbol = getUrlParameter('s');
        var feedback = "";

        if (!symbol) {
                feedback = "No search query supplied, please use the search bar to find a stock symbol";
                console.log(feedback);
                //$('#stats_go_here').html(feedback);
                $('#chart_placeholder').html(feedback);
                //$('#company_name').html("No stock specified");
                //$('#ticker_about').html("No stock specified");
                return;
        }

        var is_day = false;
        switch (history) { //check validity
                case '1d':
                        is_day = true;
                        break
                case '1w':
                case '1m':
                case '3m':
                case '1y':
                case '5y':
                case 'max':
                        //valid
                        break;
                default:
                        //TODO: Specifiy error
                        return;
        }

        if (is_day) { //unique call for 1d data
                $.ajax({
                                method: "GET",
                                crossDomain: true,
                                xhrFields: {
                                        withCredentials: true
                                },
                                url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/symbol/" + symbol,
                        })
                        .done(function(data, textStatus, xhr) {
                                console.log('Ticker data retrieved: ' + data);

                                var name = data.company;
                                var symbol = data.symbol;
                                var graphData = data.todayData;
                                var yesterday = data.yesterdayClose;

                                //$('#chart_placeholder').remove();
                                graph(graphData, '1d', false, yesterday); //defaults to 1d

                        })
                        .fail(function(xhr, textStatus, errorThrown) {
                                var statusNum = xhr.status;
                                var feedback = "";

                                switch (statusNum) {
                                        case 200: //not sure why this is considered a fail...
                                                feedback = "No results found for this ticker";
                                                break;
                                        case 401:
                                                feedback = "search: 401 (token not found, or invalid)";
                                                //TODO: redirect to sign on page?
                                                break;
                                        default:
                                                feedback = "The server returned an undefined response: Status code " + statusNum;
                                                break;
                                }
                                console.log(feedback);
                                $('#stats_go_here').html(feedback);
                                $('#chart_placeholder').html(feedback);
                        });

                return;
        }

        $.ajax({
                        method: "GET",
                        crossDomain: true,
                        xhrFields: {
                                withCredentials: true
                        },
                        url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/symbol/" + symbol + '/history/' + history,
                })
                .done(function(data, textStatus, xhr) {
                        console.log('graph data retrieved: ' + data);

                        /*
                        var name = data.company;
                        var symbol = data.symbol;
                        var stats = data.statistics;
                        var about = data.about;
                        var articles = data.articles;
                        */
                        var graphData = data.data;
                        var history = data.granularity;

                        $('#chart_placeholder').remove();
                        graph(graphData, history, false);

                })
                .fail(function(xhr, textStatus, errorThrown) {
                        var statusNum = xhr.status;
                        var feedback = "";

                        switch (statusNum) {
                                case 200: //not sure why this is considered a fail...
                                        feedback = "No results found for this ticker";
                                        break;
                                case 401:
                                        feedback = "search: 401 (token not found, or invalid)";
                                        //TODO: redirect to sign on page?
                                        break;
                                default:
                                        feedback = "The server returned an undefined response: Status code " + statusNum;
                                        break;
                        }
                        console.log(feedback);
                        $('#chart_placeholder').html(feedback);
                });
}


//date must be in yyyy-MM-dd format
//don't actually use this
function getGraphDate(date) {
        var symbol = getUrlParameter('s');
        var feedback = "";

        if (!symbol) {
                feedback = "No search query supplied, please use the search bar to find a stock symbol";
                console.log(feedback);
                //$('#stats_go_here').html(feedback);
                $('#chart_placeholder').html(feedback);
                //$('#company_name').html("No stock specified");
                //$('#ticker_about').html("No stock specified");
                return;
        }

        $.ajax({
                        method: "GET",
                        crossDomain: true,
                        xhrFields: {
                                withCredentials: true
                        },
                        url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/symbol/" + symbol + '/' + date,
                })
                .done(function(data, textStatus, xhr) {
                        console.log('graph data retrieved: ' + data);

                        /*
                        var name = data.company;
                        var symbol = data.symbol;
                        var stats = data.statistics;
                        var about = data.about;
                        var articles = data.articles;
                        */
                        var graphData = data.data;

                        $('#chart_placeholder').remove();
                        graph(graphData, '1d', false);

                })
                .fail(function(xhr, textStatus, errorThrown) {
                        var statusNum = xhr.status;
                        var feedback = "";

                        switch (statusNum) {
                                case 200: //not sure why this is considered a fail...
                                        feedback = "No results found for this ticker";
                                        break;
                                case 401:
                                        feedback = "search: 401 (token not found, or invalid)";
                                        //TODO: redirect to sign on page?
                                        break;
                                default:
                                        feedback = "The server returned an undefined response: Status code " + statusNum;
                                        break;
                        }
                        console.log(feedback);
                        $('#chart_placeholder').html(feedback);
                });
}
