<?php
ini_set('display_errors', 'On');
error_reporting(E_ALL | E_STRICT);
	if($_SERVER['REQUEST_METHOD']=='POST'){

                $id_celula = $_POST['id_celula'];
                $nome = $_POST['nome'];
                $data= $_POST['data'];
                $horario= $_POST['horario'];
                $local_prog= $_POST['local_prog'];
                $telefone= $_POST['telefone'];
                $valor= $_POST['valor'];
                
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