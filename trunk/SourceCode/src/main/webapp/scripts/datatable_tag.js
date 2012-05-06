	$(document)
				.ready(
						function() {
							// debugger;
							
							$('#${id}').on('click', '#${id} tbody tr td',
									function(event) {									
										// var id = $(this).parent().attr("id");
										// alert(id);
										// show(id);
									});
							
							oTable= $("#${id}")
									.dataTable(
											{
												"bProcessing" : true,
												"bServerSide" : true,
												"sAjaxSource" : "${source}",												
												"aoColumns" : ${ao},
												"bJQueryUI" : true,												
												"sPaginationType" : "full_numbers",
												"sDom" : '&lt;"H"&lt;"datatable_buttons"eeee&gt;fr&gt;t&lt;"F"lip&gt;',												
												"fnCreatedRow" : function(nRow,
														aData, iDataIndex) {
													if (iDataIndex == 0) {
														$(
																"#${id} thead tr td:first-child input[type=checkbox]")
																.attr(
																		'checked',
																		null);
													}
													$(nRow.firstChild)
															.html(
																	'<input type="checkbox"/>');
												}

											});		
							
							
	                         
						});
		 $(function() {
			$('.datatable_buttons')
					.html(
							'<button onclick="fnew()">New</button>'
							+'<button onclick="fshow()">Show</button>'
							+'<button onclick="fgoto()">Goto</button>'
							+'<button onclick="fdel()">Delete</button>'); 
			
			
		}); 
		
		$(function() {
			$('#${id} thead tr:first-child input[type=checkbox]')
					.on(
							'click',
							function() {
								var selected = null;
								if ($(this).attr('checked') != null) {
									selected = "selected";
								}
								$(
										'#${id} tbody tr td:first-child input[type=checkbox]')
										.attr('checked', selected);
							});
		});
		$(function() {
			debugger;
			var x  = $('${id}');
			$('#${id}').on('click', 'a',
					function(event) {
						aler('jksfjdflslfs');
						event.stopImmediatePropagation();
						return false;
					})
			addFormSubmitHandler('#table', '#table');
		});	
		var oTable;
		function fnew(){
			// debugger;
			$(panel).load("${lNew}", function() {});
			
		}
		
		function fshow(){
			// debugger;
			var s = $('#${id} tbody tr td:first-child input:checked');
			var c = $('#${id} tbody tr td:first-child input:checked').length;
			
			if(c>0){
				var idtr = $(s.parent().parent()[0]).attr('id');
				var url="${lShow}"+idtr+"?form";
				// alert(url);
				// window.location=url;
				$(panel).load(url, function() {});
			}
		}
		
		function fgoto(){
			// debugger;
			var s = $('#${id} tbody tr td:first-child input:checked');
			var c = $('#${id} tbody tr td:first-child input:checked').length;
			
			if(c>0){
				var idtr = $(s.parent().parent()[0]).attr('id');
				var url="${lShow}"+idtr;
				// alert(url);
				window.location=url;
				
			}
		}
		
		function show(id){
			var url="${lShow}"+id;
			// alert(url);
			window.location=url;
		}
		function fdel(){
			var s = $('#${id} tbody tr td:first-child input:checked');
			var c = $('#${id} tbody tr td:first-child input:checked').length;
			
			if(c>0){
				
				var idtr = $(s.parent().parent()[0]).attr('id');
				$.ajax({
			        type: "DELETE",
			        url: "${lDelete}"+idtr,
			        success:function(){			        	
			        		$("#"+idtr).html("");
			        		alert("item is deleted !");
			        	} 
			    });
			}
			debugger;			
		}