<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix = "c" uri = "http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="pt-br">
	<head>
		<%@include file="base-head.jsp"%>
	</head>
	
	<body>
		<%@include file="nav-menu.jsp"%>
		
		<div id="container" class="container-fluid">
			<h3 class="page-header">Adicionar Empresa</h3>

			<form action="${pageContext.request.contextPath}/company/${action}" method="POST">
				<input type="hidden" value="${company.getId()}" name="companyId">
				<div class="row">
					<div class="form-group col-md-4">
						<label for="content">Nome Empresa</label>
						<input type="text" class="form-control" id="name" name="name" 
							   autofocus="autofocus" placeholder="Nome da empresa" 
							   required oninvalid="this.setCustomValidity('Por favor, informe o nome da empresa.')"
							   oninput="setCustomValidity('')"
							   value="${company.getName()}">
					</div>
	
					<div class="form-group col-md-4">
						<label for="content">Cargo</label>
							<input type="text" class="form-control" id="role" name="role" 
								   placeholder="Cargo ocupado" 
								   required oninvalid="this.setCustomValidity('Por favor, informe o cargo ocupado.')"
								   oninput="setCustomValidity('')"
								   value="${company.getRole()}">
					</div>
					
					
					<div class="form-group col-md-4">
						<label for="user">Usuário</label>
						<select id="user" class="form-control selectpicker" name="user" 
							    required oninvalid="this.setCustomValidity('Por favor, informe o usuário.')"
							    oninput="setCustomValidity('')">
						  <option value="" disabled ${not empty company ? "" : "selected"}>Selecione um usuário</option>
						  <c:forEach var="user" items="${users}">
						  	<option value="${user.getId()}"  ${company.getUser().getId() == user.getId() ? "selected" : ""}>
						  		${user.getName()}
						  	</option>	
						  </c:forEach>
						</select>
					</div>
				</div>
				<hr/>
				<div class="row">
					<div class="form-group col-md-6">
						<label for="content">Data Início</label>
						<input type="date" class="form-control" id="start" name="start" 
							   autofocus="autofocus" placeholder="Início na empresa" 
							   required oninvalid="this.setCustomValidity('Por favor, informe a data de início na empresa.')"
							   oninput="setCustomValidity('')"
							   value="${company.getStart()}">
					</div>
					
					<div class="form-group col-md-6">
						<label for="content">Data Saída</label>
						<input type="date" class="form-control" id="end" name="end" 
							   autofocus="autofocus" placeholder="Saída da empresa"
							   value="${company.getEnd()}">
					</div>
				</div>
				
				<hr />
				<div id="actions" class="row pull-right">
					<div class="col-md-12">
						<a href="${pageContext.request.contextPath}/companies" class="btn btn-default">Cancelar</a>
						<button type="submit" 
								class="btn btn-primary">${not empty company ? "Alterar Empresa" : "Criar Empresa"}
						</button>
					</div>
				</div>
			</form>
		</div>
		
		<script src="js/jquery.min.js"></script>
		<script src="js/bootstrap.min.js"></script>
	</body>
</html>
