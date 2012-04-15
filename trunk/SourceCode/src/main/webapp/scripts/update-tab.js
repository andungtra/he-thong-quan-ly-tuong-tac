var isAddLinkHanlder = true;
var isAddFormSubmitHandler = true;
$(function() {
	$("#menu_menu").tabs(
			{
				ajaxOptions : {
					error : function(xhr, status, index, anchor) {
						$(anchor.hash).html(
								"Couldn't load this tab. We'll try to fix this as soon as possible. "
										+ "If this wouldn't be a demo.");
					},
				},
				load : function(event, ui) {
					$(ui.panel).off('click', 'a', tabLinkClickHandler);
					if (isAddLinkHanlder) {
						$(ui.panel).on('click', 'a', {
							ui : ui
						}, tabLinkClickHandler);
					}
					removeFormSubmitHandler(ui.panel);
					if(isAddFormSubmitHandler){
					addFormSubmitHandler(ui.panel, ui.panel);
					}
				}
			});
});
function removeFormSubmitHandler(container){
	$(container).off('submit', 'form',formSubmitHandler);
}
function tabLinkClickHandler(event) {
	var url = this.href;
	alert(url);
	if (url.indexOf('TIS/projects/ID') != -1) {
		location.open(url);
	} else {
		$(event.data.ui.panel).load(this.href, function() {
/*			addTabFormSubmitHandler();*/
		});
		event.preventDefault(event.data.ui.panel, event.data.ui.panel);
	}
}

/*function addTabFormSubmitHandler() {
	var tabContent = $('div:not(.ui-tabs-hide) > #main').parent();
	addFormSubmitHandler(tabContent);

}*/
function addFormSubmitHandler(container, resultReceiver) {
	$(container).on('submit', 'form', {resultReceiver:resultReceiver}, formSubmitHandler);
}
function formSubmitHandler(event){
	var resultReceiver = event.data.resultReceiver;
	var test = $(this).attr('method');
	if($(this).attr('method').toLowerCase() == 'get'){
		var query = $(this).serialize();
		var url = $(this).attr('action') + '?' + query;
		$(resultReceiver).load(url, function() {
		}).change();
		event.preventDefault();
	}
	if($(this).attr('method').toLowerCase() == 'post'){
		var parameters = $(this).serializeArray();
		var url = $(this).attr('action');
		$(resultReceiver).load(url, parameters, function() {
		}).change();
		event.preventDefault();
	}
}
