$(document).ready(function() {


	if ($('.header_div').eq(0).children().length < 1) { //Load header+footer manually if PHP didn't do it.
		$(".header_div").load("./header.php");
		$(".footer_div").load("./footer.php");
	}

	get_user();

	//Initialize hidden elements
	$(".hide_me_code").hide();
	$(".hide_me_project").hide();
});

function set_user(username) {
	$("#display_name").html(username);

}

function logout () {
	document.cookie = 'token=; expires=Thu, 18 Dec 2013 12:00:00 UTC; path=/; domain=.spms.westus.cloudapp.azure.com';
	window.location.href = "./index.php";
}

function register() {
	var username = $('#username').val();
	var password = $('#password').val();
	var password_confirm = $('#password_confirm').val();
	//alert('user: ' + username + '; pass: ' + password);

	$('#feedback').empty(); //clear previous errors

	if (password != password_confirm) { //ensure passwords match
		$('#feedback').html('Passwords do not match');
		return;
	}

	$.ajax({
		method: "POST",
		crossDomain: true,
		xhrFields: { withCredentials: true },
		url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/authenticate/register",
		contentType: 'application/json',
		data: JSON.stringify({
			"username": username,
			"password": password
		}),
	})
	.done(function(data, textStatus, xhr) {
		window.location.href = "./index.php";
	})
	.fail(function (xhr, textStatus, errorThrown) {
		var statusNum = xhr.status;

		switch (statusNum) {
			case 400:
				$('#feedback').html("Username already in use");
				break;
			case 500:
				$('#feedback').html("Internal server error, try registering again later.");
				break;
			default:
				$('#feedback').html("The server returned an undefined response: Status code " + statusNum);
		}
	});
}

function authenticate_user () {
	var username = $('#username').val();
	var password = $('#password').val();
	//alert('user: ' + username + '; pass: ' + password);

	$('#feedback').empty();

	$.ajax({
		method: "POST",
		crossDomain: true,
		xhrFields: { withCredentials: true },
		url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/authenticate",
		contentType: 'application/json',
		data: JSON.stringify({
			"username": username,
			"password": password
		}),
	})
	.done(function(data, textStatus, xhr) {
		window.location.href = "./index.php";
	})
	.fail(function (xhr, textStatus, errorThrown) {
		var statusNum = xhr.status;
		var feedback = "";

		switch (statusNum) {
			case 401:
				feedback = "Invalid username and/or password";
				break;
			default:
				feedback = "The server returned an undefined response: Status code " + statusNum;
				break;
		}
		console.log(feedback);
		$('#feedback').html(feedback);
	});
}

function get_user() {

	$.ajax({
		method: "GET",
		crossDomain: true,
		xhrFields: { withCredentials: true },
		url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/authenticate/getUsername",
	})
	.done(function(data, textStatus, xhr) {
		console.log('Logged-in user according to server: ' + data.username);
		set_user(data.username);
	})
	.fail(function (xhr, textStatus, errorThrown) {
		var statusNum = xhr.status;
		var feedback = "";

		switch (statusNum) {
			case 401:
				feedback = "get_user: 401 (token not found, or invalid)";

				//redirect to login or signup
				var cur_page = document.location.href.substr(document.location.href.lastIndexOf('/') + 1);
				if (cur_page != 'login.php' && cur_page != 'signup.php') {
					window.location.replace("./login.php");
				}
				break;
			case 500:
				feedback = "Auth'ed without a valid token. Guess we got hacked.";
				break;
			default:
				feedback = "The server returned an undefined response: Status code " + statusNum;
				break;
		}
		console.log(feedback);
		//$('#feedback').html(feedback);

	});
}

//TODO: return statements don't actually cause this function to return since they're inside inline functions- delete this
function is_authenticated () {
	var is_auth = -1;

	$.ajax({
		method: 'GET',
		crossDomain: true,
		xhrFields: { withCredentials: true },
		url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/authenticate/checkAuth"
	})
	.done(function() {
		is_auth = 1;
	})
	.fail(function () {
		is_auth = 0;
	})
	.always(function () {
		console.log("Auth status: " + is_auth);
		$("#auth_result").html("Auth status: " + is_auth);
		if (is_auth == 1) {
			return true;
		} else {
			return false;
		}
	});
}

function ping() {
	$.ajax({
		method: "GET",
		url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/ping",
		statusCode: {
			//successful request functions (just 2xx?) take DONE callback params; failures take FAIL callback params
			200: function (data, status) {
				$('#ping_result').html("data: " + data + "\n<br>Status: " + status);
			}
		}
	})
	.done(function(data, textStatus, xhr) {
		//handled with statusCode
	})
	.fail(function (xhr, textStatus, errorThrown) {
		var statusNum = xhr.status;
		var text = xhr.statusText; //same as textStatus param?
		$('#ping_result').html("Failed! Status: " + statusNum + "<br>Explanation: " + text);
	});
}

//Function to hide/unhide sections on click
$(".hide_me").click(function() {
	var el = this;

	if( $(el).siblings().css("display") ==  "none") {
		$(el).siblings().show("medium");
	}
	else {
		$(el).siblings().hide("medium");
	}
});

//Function to hide/unhide specific elements
function hide_me(el) {
	if( $(el).css("display") ==  "none") {

		if ($(el).attr('class') == "hide_me_code"){
			$(".hide_me_code").hide(); //hide all other codes
		} else {
			$(".hide_me_project").hide(); //hide all other projects
		}

		$(el).show("medium");

	}
	else {
		$(el).hide("medium");
	}
}

function search() {
	var input = $('#search_term').val();

	$.ajax({
		method: "GET",
		crossDomain: true,
		xhrFields: { withCredentials: true },
		url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/search?q=" + input,
	})
	.done(function(data, textStatus, xhr) {
		console.log('Found search results: ' + data);

		$('#search_list').empty(); //clear previous entries
		var option_block = '';

		for (var i = 0; i < data.length; i++) { //parse each line
			var match = '';
			var symbol = '';

			for (var j = 0; j < data[i].length; j++) { //parse data in each line
				if (j == data[i].length - 1) {
					match += data[i][j]; //special tratment for last data segment
				} else {
					match += data[i][j] + ' - '; //build up an option tag's value
				}
			}
			symbol = data[i][0];

			option_block += '<option value="' + match + '" >'; //build the entire block of options for this search

		}
		$('#search_list').append(option_block);
		$('#search_term').focus(); //forces refresh of list; add autoComplete="off" to input if it's not working

	})
	.fail(function (xhr, textStatus, errorThrown) {
		var statusNum = xhr.status;
		var feedback = "";

		switch (statusNum) {
			case 200: //not sure why this is considered a fail...
				feedback = "No results found for current search";
				/* Taken from https://stackoverflow.com/questions/30022728/perform-action-when-clicking-html5-datalist-option */
				var val = document.getElementById("search_term").value;
			    var opts = document.getElementById('search_list').childNodes;
			    for (var i = 0; i < opts.length; i++) {
			      if (opts[i].value === val) {
			        // An item was selected from the list!
			        // yourCallbackHere()
					var symbol = val.split(" - ")[0];
			        goToTicker(symbol);
					console.log("search match selected!")
			        break;
			      }
			    }
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
		//$('#feedback').html(feedback);

	});
}

function goToTicker(symbol) {
	window.location.href = "./ticker.php?s=" + symbol;
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

function setupNews(articles) {
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
}


function articleGet(n_or_p) {
	var symbol = getUrlParameter('s');
	var cur_page = parseInt($("article_page_number").html());

	/* Calc next or previous page number */
	if (n_or_p == 'p') {
		cur_page --;
	} else {
		cur_page++;
	}

	/* Wrap article pages, display new page number */
	if (cur_page > 3) {
		cur_page = 1;
	} else if (cur_page < 1) {
		cur_page = 3;
	}
	$("#article_page_number").html(cur_page);

	/* Clear previous articles */
	$("#news_articles").find(".article_url").not("#article_template").remove();


	$.ajax({
		method: "GET",
		crossDomain: true,
		xhrFields: { withCredentials: true },
		url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/symbol/" + symbol + "/news/" + cur_page,
	})
	.done(function(data, textStatus, xhr) {
		console.log('Article data retrieved: ' + data);

		var articles = data.articles;

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
	});
}


function buyStock(action) {
	var date = $("#datepicker").val();
	var ammount;
	//var price = $("#buyPrice");
	
	if (action == 'sell') { //handle selling stocks
		ammount = $("#sellcount").val() * -1;
	} else {
		ammount = $("#buycount").val();
	}
	
	var symbol = $('#stockSymbol').html(); //only works after ticker data has loaded
	
	
	$.ajax({
		method: "POST",
		crossDomain: true,
		xhrFields: { withCredentials: true },
		url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/portfolio/add/" + symbol,
		data: JSON.stringify({
			"symbol": symbol,
			"date": date,
			"shares": ammount
		}),
	})
	.done(function(data, textStatus, xhr) {
		var feeback = "Stock shares successfully added to or removed from your portfolio";
		
		console.log(feedback);
		$('#buyFeedback').html(feedback);
	})
	.fail(function (xhr, textStatus, errorThrown) {
		var statusNum = xhr.status;
		var feedback = "";

		switch (statusNum) {
			case 200: //not sure why this is considered a fail...
				feedback = "Status code 200 returned as a failure";
				break;
			case 401:
				feedback = "search: 401 (token not found, or invalid)";
				//TODO: redirect to sign on page?
				break;
			default:
				feedback = "The server returned an undefined response: Status code " + statusNum;
				break;
		}
		$('#buyFeedback').html(feedback)
		console.log(feedback);
	});
	
}
 
 
//It was set up to use the /portfolio endpoint for some reason
function dashboard() {
	
	
	$.ajax({
		method: "GET",
		crossDomain: true,
		xhrFields: { withCredentials: true },
		url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/portfolio",
	})
	.done(function(data, textStatus, xhr) {
		console.log(JSON.stringify(data));
		
		var stock_table = data.portfolio;
		var watch_table = data.watchlist;
		var graphData = data.timesweries;
		var total_value = data.value;
		var articles = data.news;
		
		//Handle the stock table
		var i;
		var new_table = "<tr><th>Stock</th><th>Shares</th></tr>";
		var url;
		var link;

		//dynamically generate tables based on server data
		for (key in stock_table) {
			url = 'http://spms.westus.cloudapp.azure.com/ticker.php?s=' + stock_table[key];
			link = '<a href="' + url + '">';
			
			new_table += "<tr>";
			new_table += "<td>" + key + "</td>";
			new_table += link + "<td>" + stock_table[key] + "</td>" + "</a>";

			new_table += "</tr>"
		}
		//new_table += "</table>"

		//insert the rows into the table
		$("#db_stock_table").html(new_table);
		
		
		//Handle the wishlist table
		var new_table = "<tr><th>Stock</th></tr>";
		for (i = 0; i < watch_table.length; i++) {
			new_table += "<tr>";
			//new_table += "<td>" + key + "</td>";
			new_table += "<td>" + watch_table[i] + "</td>";

			new_table += "</tr>"
		}
		
		$("#db_watch_table").html(new_table);
		
		setupNews(articles);
		
		/*
		var feeback = "Stock shares successfully added to your portfolio";
		
		console.log(feedback);
		$('#buyFeedback').html(feedback);
		*/
	})
	.fail(function (xhr, textStatus, errorThrown) {
		var statusNum = xhr.status;
		var feedback = "";

		switch (statusNum) {
			case 200: //not sure why this is considered a fail...
				feedback = "Status code 200 returned as a failure";
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
	});
}










/* LOCAL CODE THAT DOESN'T NEED TO ACCESS THE DATABASE -----------------------*/

/* Clears all input fields on the page */
function clear_inputs() {
	$(":input").each(function() {
		//Avoid clearing username and character list fields
		if ($(this).attr("id") == "pName") {
			return;
		}
		if ($(this).attr("id") == "char_list") {
			return;
		}

		switch(this.type) {
			case "password":
			case "text":
			case "textarea":
			case "file":
			case "select-one":
			case "select-multiple":
			case "date":
			case "number":
			case "tel":
			case "email":
				$(this).val("");
				break;
			case "checkbox":
			case "radio":
				this.checked = false;
				break;
		}
	});
}


/* Escapes all regex special characters in a string */
function escape_regex(str) {
	return str.replace(/([;&,\.\+\*\~':"\!\^#$%@\[\]\(\)=>\|])/g, '\\$1');
}
