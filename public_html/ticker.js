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
		
		//fill in company Name and About
		$("#company_name").html(name);
		$("#ticker_about").html(about);
		
		var i, j;
		var new_table = "<table class='table stock_tables'>";
		new_table += "<tr><th>Statistics</th></tr>";
		
		//console.log("Stats length: " + stats.length + "; Row length: " + stats[1].length);
		
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
			
			$(new_article).find(".artcile_img").html("<img src=" + articles[i].image + ">");
			$(new_article).find(".article_headline").html(articles[i].headline);
			$(new_article).find(".article_summary").html(articles[i].summary);
			$(new_article).attr("href", articles[i].url);
			$("#news_articles").append(new_article);
		}
		
		$("#article_page_number").html("1");
		
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


function generateData(tbl) {
        var arr = [];

        for (var i = 0; i < tbl.length; i++) {
                if (tbl[i]['date'].substr(14,2) % 1 == 0) {
                        arr.push(tbl[i]['marketAverage']);
                }
        }

        return arr;
}

function generateLabels(tbl) {
        var arr = [];

        for (var i = 0; i < tbl.length; i++) {
                if (tbl[i]['date'].substr(14,2) % 1 == 0) {
                        arr.push(tbl[i]['date'].substr(11,5));
                }
        }

        return arr;
}

function graph(graphData) { //pass in data.todayData from AJAX request
    var ctx = document.getElementById('myChart').getContext('2d');
    var tbl = graphData;

    var data = {
            labels: generateLabels(tbl),
            datasets: [{
                    data: generateData(tbl),
                    hidden: false
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
                            tension: 0
                    }
            }
    };

    var chart = new Chart(ctx, {
            type: 'line',
            data: data,
            options: options
    });
}
