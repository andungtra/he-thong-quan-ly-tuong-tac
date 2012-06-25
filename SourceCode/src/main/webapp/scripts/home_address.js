function changeAddress(url){
	var regExp = /\/projects\/\d+/;
	if(url.search(regExp) > -1){
		window.open(url, "_blank");
		return;
	}
	var regExp = /\/accounts\/\d+/;
	if(url.search(regExp) > -1 && document.URL.search(regExp) > -1){
		if(url.match(regExp)[0] == document.URL.match(regExp)[0]){
			var deepLink = url.substr(url.search(regExp));
			$.address.value(deepLink.replace(regExp, ''));
			return;
		}
	}
	$.address.value(url);
}
function addressChangeHandler(event) {
	debugger;
	// do something depending on the event.value property, e.g.
	var url = event.value;
	if(url == '/'){
		$.address.value('dashboard');
		return;
	}
		$(panel).mask("Loading...");
		var track = $.address.baseURL().replace('/home', '/') + url;
		$(panel).load(track, function() {
			$(panel).unmask();
		});
}