//Encourage AJAX to always fetch newest data and not rely on cache (doesn't always work)
$.ajaxSetup({ cache: false });

if ($('.header_div').eq(0).children().length < 1) { //Load header+footer manually if PHP didn't do it.
	$(".header_div").load("./header.php");
	$(".footer_div").load("./footer.php");
}

$( document ).ready(function() {
	//Initialize hidden elements
	$(".hide_me_code").hide();
	$(".hide_me_project").hide();
});

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
