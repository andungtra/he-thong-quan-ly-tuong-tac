function changeAddress(url){
	var regExp = /\/projects\/\d+/;
	if(url.search(regExp) > -1 && document.URL.search(regExp) > -1){
		if(url.match(regExp)[0] == document.URL.match(regExp)[0]){
			var deepLink = url.substr(url.search(regExp));
			$.address.value(deepLink.replace(regExp, ''));
			return;
		}
	}
	window.open(url, "_blank");
}
function addressChangeHandler(event) {
	// do something depending on the event.value property, e.g.
	debugger;
	var url = event.value;
	if(url == '/'){
		$.address.value('overview');
		return;
	}
	if(url.search(/\/overview/) > -1){
		$("#menu_menu").tabs( "select" , 0);
	}
	if(url.search(/\/workitems/) > -1){
		$("#menu_menu").tabs( "select" , 1);
	}
	if(url.search(/\/dumpcalendar/) > -1){
		$("#menu_menu").tabs( "select" , 2);
	}
	if(url.search(/\/roadmap/) > -1){
		$("#menu_menu").tabs( "select" , 3);
	}
	if(url.search(/\/members/) > -1){
		$("#menu_menu").tabs( "select" , 4);
	}
		$(panel).mask("Loading...");
		var track = $.address.baseURL() + url;
		$(panel).load(track, function() {
			$(panel).unmask();
		});
}