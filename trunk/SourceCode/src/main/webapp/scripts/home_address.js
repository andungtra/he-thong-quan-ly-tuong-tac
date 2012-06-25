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
	var tabMappings = ['/dashboard','/dumpcalendar','/projects'];
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
		var track = $.address.baseURL().replace('/home', '/') + url;
		$(panel).load(track, function() {
			$(panel).unmask();
		});
}