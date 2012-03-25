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
						addFormSubmitHandler();
					}
				},
				load : function(event, ui) {
					$(ui.panel).delegate('a', 'click', function(event) {
						$(ui.panel).load(this.href, function() {
							addFormSubmitHandler();
						});
						event.preventDefault();
					});
				}
			});
	function addFormSubmitHandler() {
		var test = $("form");
		$("form[method=GET]").bind('submit', function(event) {
			var tabContent = $('#main').parent();
			var query = $(this).serialize();
			var url = $(this).attr('action') + '?' + query;
			$(tabContent).load(url, function() {
				addFormSubmitHandler();
			}).change();
			event.preventDefault();

		});
		
		$("form[method=POST]").bind('submit', function(event) {
			var tabContent = $('#main').parent();
			var parameters = $(this).serializeArray();
			var url = $(this).attr('action');;
			$(tabContent).load(url,parameters, function() {
				addFormSubmitHandler();
			}).change();
			event.preventDefault();

		});
	}

});