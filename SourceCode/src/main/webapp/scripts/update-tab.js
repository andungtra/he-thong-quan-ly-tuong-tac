var addLinkHanlder = true;
$(function() {
	$("#menu_menu").tabs(
			{
				ajaxOptions : {
					error : function(xhr, status, index, anchor) {
						$(anchor.hash).html(
								"Couldn't load this tab. We'll try to fix this as soon as possible. "
										+ "If this wouldn't be a demo.");
					},
					success : function() {
						addTabFormSubmitHandler();
					}
				},
				load : function(event, ui) {
					if (addLinkHanlder) {
						$(ui.panel).off('click', 'a', tabLinkClickHandler);
						$(ui.panel).on('click', 'a', {
							ui : ui
						}, tabLinkClickHandler);
					}
				}
			});
});
function tabLinkClickHandler(event) {
	var url = this.href;
	alert(url);
	if (url.indexOf('TIS/projects/ID') != -1) {
		location.open(url);
	} else {
		$(event.data.ui.panel).load(this.href, function() {
			addTabFormSubmitHandler();
		});
		event.preventDefault();
	}
}

function addTabFormSubmitHandler() {
	var tabContent = $('div:not(.ui-tabs-hide) > #main').parent();
	addFormSubmitHandler(tabContent);

}
function addFormSubmitHandler(resultReceiver) {
	$("form[method=GET]").bind('submit', function(event) {
		var query = $(this).serialize();
		var url = $(this).attr('action') + '?' + query;
		$(resultReceiver).load(url, function() {
			addFormSubmitHandler();
		}).change();
		event.preventDefault();

	});

	$("form[method=POST]").bind('submit', function(event) {
		var parameters = $(this).serializeArray();
		var url = $(this).attr('action');
		$(resultReceiver).load(url, parameters, function() {
			addFormSubmitHandler();
		}).change();
		event.preventDefault();

	})
}
