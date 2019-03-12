var num_copies = 1;

function copy_table() {
	/* Copy the table to the end of the body element and save it in a variable */
	var new_table = $(".copy_me").eq(0).clone().appendTo("body");

	/* Prepend the copy # to the new table's header;
	.eq(0) is used to select the first result if multiple matches are found */
	$(new_table).find("th").eq(0).prepend("Table Copy " + num_copies + ": ");
	
	num_copies++;
}
