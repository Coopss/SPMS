var user = null;

$(document).ready(function() {
	if ($('.header_div').eq(0).children().length < 1) { //Load header+footer manually if PHP didn't do it.
		$(".header_div").load("./header.php");
		$(".footer_div").load("./footer.php");
	}
	
	if (!is_authenticated()) { //check for logged in status on every page load
		user = null;
	}
	
	//Initialize hidden elements
	$(".hide_me_code").hide();
	$(".hide_me_project").hide();
});

function authenticate_user () {
	var username = $('#username').val();
	var password = $('#password').val();
	//alert('user: ' + username + '; pass: ' + password);
	
	/* make sure server is ready before sending actual authentication */
	$.ajax({
		method: 'GET',
		crossDomain: true,
		xhrFields: { withCredentials: true },
		url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/authenticate/ping"
	})
	.fail(function (xhr, textStatus, errorThrown) { //stop if an error occurs with ping
		var statusNum = xhr.status;
		var text = xhr.statusText; //same as textStatus param?
		$('#ping_result').html("Authentication ping failed! Status: " + statusNum + "<br>Explanation: " + text);
		return;
	});
	
	
	$.ajax({
		method: "POST",
		crossDomain: true,
		xhrFields: { withCredentials: true },
		url: "http://spms.westus.cloudapp.azure.com:8080/SPMS/api/authenticate",
		contentType: 'application/json',
		crossDomain: true,
		data: JSON.stringify({
			"username": username,
			"password": password
		}),
	})
	.done(function(data, textStatus, xhr) {
		$('#validate_result').html("data: " + data + "\n<br>Status: " + status + '<br>StatusCode: ' + xhr.status);
	})
	.fail(function (xhr, textStatus, errorThrown) {
		var statusNum = xhr.status;
		var text = xhr.statusText; //same as textStatus param?
		$('#validate_result').html("statusNum: " + statusNum + '; text: ' + textStatus + '; Error: ' + errorThrown);
		return;
	});
}

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
	
	/* GET function;
	$.get("http://spms.westus.cloudapp.azure.com:8080/SPMS/api/ping", function(data, status) {
		//alert("data: " + data + "\n\nStatus: " + status);
		if (status == 'success') {
			$('#ping_result').html("data: " + data + "\n<br>Status: " + status);
			//alert("data: " + data + "\n\nStatus: " + status);
		} else {
			alert('idk');
		}
	});
	*/
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
