$(function(){
		$('#form_advanced_panel').hide();
			$('#form_advanced_panel_link').click(function(event){
				togglecustom(this);
			event.preventDefault();
	});
	});
	function togglecustom(obj){
		if($(obj).filter('.show').length > 0){
			$('#form_advanced_panel').show('blind');
		}else{
			$('#form_advanced_panel').hide('blind');
		}
		$('#form_advanced_panel_link').toggleClass('show');
		$('#form_advanced_panel_link img').toggleClass('hidden_arrow');
	}