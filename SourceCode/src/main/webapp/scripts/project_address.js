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
	var tabMappings = ['/overview','/workitems','/dumpcalendar', '/iterations', '/memberinformations', '/?form'];
	debugger;
	var url = event.value;
	if(url == '/'){
		$.address.value(tabMappings[0]);
		return;
	}
	for(var index = 0; index < tabMappings.length; ++index){
		if(url.search(tabMappings[index]) > -1){
			$("#menu_menu").tabs( "select" , index);
			break;
		}
	}
		$(panel).mask("Loading...");
		var track = $.address.baseURL() + url;
		$(panel).load(track, function() {
			$(panel).unmask();
		});
}