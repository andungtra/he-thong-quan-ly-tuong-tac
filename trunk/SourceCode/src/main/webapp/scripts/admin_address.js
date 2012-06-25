function changeAddress(url){
	debugger;
/*	var regExp = /\/projects\/\d+/;
	if(url.search(regExp) > -1){
			window.open(url, "_blank");
			return;
	}
	$.address.value(url.replace('/TIS', ''));*/
	if(url.search(/\?goto=true/) > -1){
		window.open(url.replace('?goto=true',''), "_blank");
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
	for(var index = 0; index < tabMappings.length; ++index){
		if(url.search(tabMappings[index]) > -1){
			$("#menu_menu").tabs( "select" , index);
			break;
		}
	}
		$(panel).mask("Loading...");
		var track = $.address.baseURL().replace('/administrator', '') + url;
		$(panel).load(track, function() {
			$(panel).unmask();
		});
}