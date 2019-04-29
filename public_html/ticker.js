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
		xhrFields: { withCredentials: true },
		url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/symbol/" + symbol,
	})
	.done(function(data, textStatus, xhr) {
		console.log('Ticker data retrieved: ' + data);

		var name = data.company;
		var symbol = data.symbol;
		var graphData = data.todayData;
		var stats = data.statistics;
		var about = data.about;
		var articles = data.articles;
		var close = data.yesterdayClose;

		//fill in company Name and About
		$("#company_name").html(name + ' (<span id="stockSymbol">' + symbol + '</span>)');
		$("#ticker_about").html(about);

		var i, j;
		var new_table = "<table class='table stock_tables'>";
		new_table += "<tr><th>Statistics</th></tr>";

		//dynamically generate tables based on server data
		for (key in stats) {
			new_table += "<tr>";
			new_table += "<td>" + key + "</td>";
			new_table += "<td>" + stats[key] + "</td>";

			new_table += "</tr>"
		}
		new_table += "</table>"

		//insert the table into the page
		$("#stats_go_here").html(new_table);


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

        $('#chart_placeholder').remove();
		graph(graphData);

	})
	.fail(function (xhr, textStatus, errorThrown) {
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


function generateData(tbl, labels, yesterdayClose) {
	var arr = [];
	var i, index, last, lastDate, date;

	last = yesterdayClose;
	lastDate = moment().year(1902);

	for (i = 0; i < tbl.length; i++) {
		date = moment(tbl[i]['date']);
		if (Number(lastDate) < Number(date)) {
			lastDate = date;
		}

		if ((index = labels.map(Number).indexOf(+moment(tbl[i]['date']))) != -1) {
			arr[index] = Number(tbl[i]['marketAverage']);
		}
	}

	for (i = 0; i < labels.length; i++) {
		if (arr[i] != null) {
			last = arr[i];
		} else if ((Number(labels[i]) < Number(lastDate))) {
			arr[i] = last;
		} else {
			break;
		}
	}

	return arr;
}

//TODO: add parameter to change labels based on view (day, week, month, etc.)
function generateLabels(history = '1d') {
	var arr = [];
	var currDate = moment().tz("America/New_York");
	var date, max, inc;

	switch (history) {
		case '1w':
			inc = 1;
			max = 7;
			break;
		case '1d':
		default:
			inc = 1;
			max = 420;
			break;
	}
	for (var i = 0; i < max; i += inc) {
		date = moment(currDate.format());
		switch (history) {
			case '1w':

			case '1d':
			default:
				date.set({'hour': (i / 60) + 9, 'minute': i % 60, 'second': 0, 'millisecond': 0});
				break;
		}
		arr.push(date);
	}

	return arr;
}

function setOpenPrice(tbl) {
	var openIndex;
	var earliestDate = moment().tz("America/New_York").valueOf();
	for (var i = 0; i < tbl.length; i++) {
		if (moment(tbl[i]['date']).valueOf() < earliestDate) {
			openIndex = i;
			earliestDate = moment(tbl[i]['date']).valueOf();
		}
	}

	return Number(tbl[openIndex]['marketAverage']);
}

function chooseColor(tbl, openPrice) {
	var lastPrice, lastDate, date;
	lastDate = moment().year(1902);

	for (var i = 0; i < tbl.length; i++) {
		date = moment(tbl[i]['date']);
		if (date.valueOf() >= lastDate.valueOf()) {
			lastDate = date;
			lastPrice = Number(tbl[i]['marketAverage']);
		}
	}

	if (openPrice >= lastPrice) {
		return 'rgba(244, 85, 49, 0.5)'; //red
	} else {
		return 'rgba(33, 206, 153, 0.5)'; //green
	}
}

function graph(graphData) { //pass in data.todayData from AJAX request
    var ctx = document.getElementById('myChart').getContext('2d');

    var tbl = graphData;
    var openPrice = setOpenPrice(tbl);
    var graphColor = chooseColor(tbl, openPrice);
    var labels = generateLabels();

    var data = {
            labels: labels,
            datasets: [{
                    data: generateData(tbl, labels, openPrice),
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
			    type:'time',
			    distribution: 'series',
			    time: {
				    unit: 'minute'
			    },
			    bounds: 'ticks'
		    }]
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

	switch (history) { //check validity
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

	$.ajax({
		method: "GET",
		crossDomain: true,
		xhrFields: { withCredentials: true },
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

        $('#chart_placeholder').remove();
		graph(graphData);

	})
	.fail(function (xhr, textStatus, errorThrown) {
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
		xhrFields: { withCredentials: true },
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
		graph(graphData);

	})
	.fail(function (xhr, textStatus, errorThrown) {
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
