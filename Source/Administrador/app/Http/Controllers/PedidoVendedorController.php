<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;

use App\Http\Requests;
use App\Http\Controllers\Controller;

use Illuminate\Support\Facades\DB;
use App\Models\Property as Property;
use DateTime;

class PedidoVendedorController extends Controller
{
    /**
     * Display a listing of the resource.
     *
     * @return Response
     */
    public function index(Request $request)
    {		
			$vendedores = DB::select("select nombre,id from usuarios where privilegio = 2 order by nombre");
			$clientes = DB::select("select nombre,id from clientes order by nombre");
			
			
			$fecha2 = $request->datepicker;
			$idCliente = $request->idCliente ;
			$idVendedor =$request->idVendedor ;
			
			$sql = "select * from productos left join pedidos on pedidos.id_producto = productos.id 
											left join compras on pedidos.id_compra = compras.id_compra ";
			
			if( (strcmp($request->idCliente, 'Todos')) || (strcmp($request->idVendedor ,'Todos')) || (strcmp($request->datepicker , 'Todas'))){
				$sql .= " where";
				$contador = 0;
				if(strcmp($request->idCliente, 'Todos')){
					$contador += 1;
					$sql .= " compras.id_cliente = " . $request->idCliente ;
				}
				if(strcmp($request->idVendedor ,'Todos')){
					if($contador > 0)
						$sql .= " and ";
					$contador += 1;
					$sql .= " compras.id_usuario = " . $request->idVendedor ;
				}
				if(strcmp($request->datepicker , 'Todas')){
					if($contador > 0)
						$sql .= " and ";
					$dt = new DateTime($fecha2);
					$fecha = $dt->format('Y-m-d');
					$sql .= " compras.fecha = '" . $fecha ." 00:00:00'";
				}
			}
			$pedidos = DB::select($sql);
			
			$bultos = DB::select("$sql group by compras.id_compra");

                        
        return view('pedidos.pedidovendedor', ['title' => 'Home',
                                'page' => 'home','pedidos' => $pedidos, 'clientes' => $clientes, 'bultos' => $bultos, 'vendedores' => $vendedores,
                                 'idVendedor' => $idVendedor, 'idCliente' => $idCliente, 'fecha2' => $fecha2]
        );
        
        
    }
}
