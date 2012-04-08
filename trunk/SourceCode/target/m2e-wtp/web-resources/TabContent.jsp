<div id="main">
    <jsp:directive.page contentType="text/html;charset=UTF-8"/>
    <jsp:output omit-xml-declaration="yes"/>
	<div class="content">
		<tiles:insertAttribute name="body" />
	</div>
	<tiles:insertAttribute name="footer" ignore="true" />
</div>