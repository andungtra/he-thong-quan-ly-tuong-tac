$(function(){
		$('#form_advanced_panel').hide();
			$('#form_advanced_panel_link').click(function(event){
			toggle(this);
			event.preventDefault();
	});
	});
	function toggle(obj){
		if($(obj).filter('.show').length > 0){
			$('#form_advanced_panel').show('blind');
		}else{
			$('#form_advanced_panel').hide('blind');
		}
		$('#form_advanced_panel_link').toggleClass('show');
		$('#form_advanced_panel_link img').toggleClass('hidden_arrow');
	}