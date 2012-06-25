function changeAddress(url){
	debugger;
	var regExp = /\/projects\/\d+/;
	if(url.search(regExp) > -1){
			window.open(url, "_blank");
			return;
	}
	$.address.value(url.replace('/TIS', ''));
}
function addressChangeHandler(event) {
	// do something depending on the event.value property, e.g.
	var tabMappings = ['/projects','/studyclasses','/accounts', '/projectprocesses'];
	debugger;
	var url = event.value;
	if(url == '/'){
		$.address.value(tabMappings[0]);
		return;
	}
		$(panel).mask("Loading...");
		var track = $.address.baseURL().replace('/administrator', '') + url;
		$(panel).load(track, function() {
			$(panel).unmask();
		});
}