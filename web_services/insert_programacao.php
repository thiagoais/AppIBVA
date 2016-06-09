<?php
ini_set('display_errors', 'On');
error_reporting(E_ALL | E_STRICT);
	if($_SERVER['REQUEST_METHOD']=='POST'){

                $id_celula = $_POST['id_celula'];
                $nome = utf8_decode($_POST['nome']);
                $data= utf8_decode($_POST['data']);
                $horario= utf8_decode($_POST['horario']);
                $local_prog= utf8_decode($_POST['local_prog']);
                $telefone= utf8_decode($_POST['telefone']);
                $valor= utf8_decode($_POST['valor']);
                
                if (isset($_POST['image'])) {
		    $image = $_POST['image'];
		}
		
		require_once('dbConnect.php');
		
		if (isset($_POST['image'])) {
		    $sql = "INSERT INTO programacao (id_celula, nome, data_prog, horario, local_prog, telefone, valor, imagem) VALUES (?,?,?,?,?,?,?,?)";
		} else {
		    $sql = "INSERT INTO programacao (id_celula, nome, data_prog, horario, local_prog, telefone, valor) VALUES (?,?,?,?,?,?,?)";
		}
		
		$stmt = mysqli_prepare($con,$sql);
		
		if (isset($_POST['image'])) {
		    mysqli_stmt_bind_param($stmt,"isssssss",$id_celula, $nome, $data, $horario, $local_prog, $telefone, $valor, $image);
		} else {
		    mysqli_stmt_bind_param($stmt,"issssss",$id_celula, $nome, $data, $horario, $local_prog, $telefone, $valor);
		}
				
		mysqli_stmt_execute($stmt);
		
		$check = mysqli_stmt_affected_rows($stmt);
		
		if($check == 1){
			echo "0";
		}else{
			echo "Error Uploading Image";
		}
		mysqli_close($con);
	}else{
		echo "Error";
	}