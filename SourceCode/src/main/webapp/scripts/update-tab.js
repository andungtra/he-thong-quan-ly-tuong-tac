var panel = null;
// create ui tab.
$(function() {
	$("#menu_menu").tabs(
			{
				ajaxOptions : {
					error : function(xhr, status, index, anchor) {
						$(anchor.hash).html(
								"Couldn't load this tab. We'll try to fix this as soon as possible. "
										+ "If this wouldn't be a demo.");
					},
					beforeSend : function(jqXHR, settings){
						$(panel).mask("Loading...");
					}
				},
				load : function(event, ui) {
					panel = ui.panel;
				},
			});
});
// hook global link click event.
$(function() {
	$('#menu_menu').on('click', 'a', function(event) {
		var url = this.href;
		$(panel).load(this.href, function() {
		});
		return false;
	});
});
// hook global form submit event
$(function(){
	$('#menu_menu').on('submit', 'form', function(event) {
		formSubmitHandler(event, panel);
	});
});
function removeFormSubmitHandler(container) {
	$(container).off('submit', 'form', formSubmitHandler);
}

/*
 * function addTabFormSubmitHandler() { var tabContent =
 * $('div:not(.ui-tabs-hide) > #main').parent();
 * addFormSubmitHandler(tabContent); }
 */
function addFormSubmitHandler(container, resultReceiver) {
	$(container).on('submit', 'form', {
		resultReceiver : resultReceiver
	}, formSubmitHandler);
}
function formSubmitHandler(event, receiver) {
	var resultReceiver = receiver;
	if(resultReceiver == null){
		resultReceiver = event.data.resultReceiver;
	}
	var form = $(event.target)
	if (form.attr('method').toLowerCase() == 'get') {
		var query = $(this).serialize();
		var url = form.attr('action') + '?' + query;
		$(resultReceiver).load(url, function() {
		}).change();
		event.preventDefault();
	}
	if (form.attr('method').toLowerCase() == 'post') {
		var parameters = form.serializeArray();
		var url = form.attr('action');
		$(resultReceiver).load(url, parameters, function() {
		}).change();
		event.preventDefault();
	}
}