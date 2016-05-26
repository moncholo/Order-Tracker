<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;

use Illuminate\Support\Facades\DB;
use App\Models\Property as Property;

class ProductosController extends Controller
{
    public function index()
    {
    	
		  $sql = "SELECT u.id, u.nombre, u.codigo, u.stock, u.marca, MIN(t.imagen_base64) AS imagen_base64,m.nombre as nombreMarca, c.nombre as nombreCategoria FROM productos u Left JOIN imagenes t ON t.id_producto = u.id 
		  Left JOIN marcas m ON m.id = u.marca
		  Left Join categorias c ON c.id= u.categoria GROUP BY u.id;";

		  $productos = DB::select($sql);
		  $marcas = DB::select("select * from marcas");

        return view('productos.productos', ['title' => 'Home',
                                'page' => 'home','productos' => $productos,'marcas'=> $marcas, 'idMarca'=>0, 'nombre'=>'','codigo'=>'']
        );  
    }
    
    public function eliminar($id)
    {
		DB::delete("delete from productos where id=$id");
		DB::delete("delete from imagenes where id_producto=$id");
		
		return redirect('/productos');
	}
	
	
    public function filtro(Request $request)
    {	
		
			$sql = "SELECT u.id, u.nombre, u.codigo, u.stock, u.marca, MIN(t.imagen_base64) AS imagen_base64,m.nombre as nombreMarca, c.nombre as nombreCategoria FROM productos u Left JOIN imagenes t ON t.id_producto = u.id 
		  Left JOIN marcas m ON m.id = u.marca
		  Left Join categorias c ON c.id= u.categoria where 1=1";
			
			$marcas = DB::select("select * from marcas");
			
			$idMarca = $request->idMarca;
			$nombre = $request->nombre;
			$codigo = $request->codigo;
			
			if ($codigo != "" || $idMarca != 0 || $nombre != ""){ 
				
				if ($idMarca != 0)
					$sql .= " and u.marca=$idMarca";
					
				if ($nombre != "")
				    $sql.= " and u.nombre='$nombre'";
					
				if ($codigo != "")
					$sql.= " and u.codigo='$codigo'";
			}
			
			$sql .= " GROUP BY u.id;";
			
			$productos = DB::select($sql);
			
		return view('productos.productos', ['title' => 'Home',
               'page' => 'home','productos' => $productos,'marcas'=> $marcas, 'idMarca'=>$idMarca, 'nombre'=>$nombre,'codigo'=>$codigo]
        );   
	}
	
	
}
