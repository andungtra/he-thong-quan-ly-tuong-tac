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
					if (isAddLinkHanlder) {
						addLinkClickHanlder(ui.panel, ui.panel);
					}
					if(isAddFormSubmitHandler){
					addFormSubmitHandler(ui.panel, ui.panel);
					}
					isAddLinkHanlder = true;
					isAddFormSubmitHandler = true;
				},
				spinner: 'Retrieving data...',
				select : function(event, ui){
					$(ui.panel).off('click', '**');
					$(ui.panel).off('submit', '**');
				}
			});
});
function addLinkClickHanlder(container, target){
	$(container).on('click', 'a', {target:target}, linkClickHandler);
}
function removeLinkClickHanlder(container){
	$(container).off('click', 'a', linkClickHandler);
}
function removeFormSubmitHandler(container){
	$(container).off('submit', 'form',formSubmitHandler);
}
function linkClickHandler(event) {
	var url = this.href;
	alert(url);
	if (url.indexOf('TIS/projects/ID') != -1) {
		location.open(url);
	} else {
		$(event.data.target).load(this.href, function() {
/*			addTabFormSubmitHandler();*/
		});
		event.preventDefault();
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
