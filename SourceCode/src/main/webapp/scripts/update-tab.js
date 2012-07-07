var panel = null;
// create ui tab.
var testIndex = 0;
var changed = 0;
var tabUrl = null;
var reallyClick = true;
var operationSucess = false;
var triggerAddressHandler = true;
var firstTime = true;
$(function() {
	$("#menu_menu").tabs(
			{
				create: function(event, ui) {
					
				},
				ajaxOptions : {
					beforeSend : function(jqXHR, settings) {
						tabUrl = settings.url;
						return false;
					}
				},
				select : function(event, ui) {
					if (changed == 1) {
						if (!confirm("You will lose unsaved changes. Are you sure?")) {
							return false;
						} else {
							changed = 0;
							$(panel).html('');
							return true;
						}
					}else{
						$(panel).html('');
					}
				},
				show : function(event, ui) {
					panel = ui.panel;
					if (reallyClick == true && !firstTime) {
						changeAddress(tabUrl);
					}
					reallyClick = true;
					firstTime = false;
					return false;
				},
				load : function(event, ui) {
					var x;
					$(ui.panel).unmask();
				}
			});
});

$(function() {
	if (!(window.notAddHandler === undefined) && notAddHandler) {
		// hook global link click event.
		$('#menu_menu').on('click', 'a', function(event) {
			var fullUrl = $(this).attr('href');
			changeAddress(fullUrl);
			return false;
		});
		// hook global form submit event
		$('#menu_menu').on('submit', 'form', function(event) {
			formSubmitHandler(event, panel);
			return false;
		});
		notAddHandler = false;
	}
});
$(function() {
	$.address.change(function(event){
		if(triggerAddressHandler){
		reallyClick = false;
		addressChangeHandler(event);
		reallyClick = true;
		}
		triggerAddressHandler = true;
	});
})
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
	debugger;
	var resultReceiver = receiver;
	if (resultReceiver == null) {
		resultReceiver = event.data.resultReceiver;
	}
	var form = $(event.target);
	if (form.attr('method').toLowerCase() == 'get') {
		var query = $(this).serialize();
		var url = form.attr('action') + '?' + query;
		$(resultReceiver).load(url, function() {
			$(panel).unmask();
		}).change();
		event.preventDefault();
	}
	if (form.attr('method').toLowerCase() == 'post') {
		var parameters = form.serializeArray();
		var url = form.attr('action');
		operationSucess = false;
		$(resultReceiver).load(url, parameters, function() {
			$(panel).unmask();
			if(operationSucess){
				var deeplink = $.address.value();
				triggerAddressHandler = false;
				var formRexg = /\d*\?/;
				var index = deeplink.search(formRexg);
				if(index > -1){
				deeplink = deeplink.substr(0, index);
				$.address.value(deeplink);
				}
			}
		}).change();
		event.preventDefault();
	}
	form.mask("Loading...");
	return false;
}