@extends("layouts.base")

@section("head")

	


  <script type="text/javascript">
    $(document).ready(function(){
      $(document).keypress(
          function(event){
            if (event.which == '13') {
              event.preventDefault();
            }
      });
    });
  </script>

    
@section("content")
 
		
    <section id="search-section">
			<a  href="{{app()->make('urls')->getUrlUsuarios()}}" class="btn btn-primary btn-block"><h4><b>VENDEDORES</b></h4></a>

		
	<h4><form action="{{app()->make('urls')->getUrlFiltroUsuarios()}}" method="POST" class="form-horizontal"   enctype="multipart/form-data">
		<input type="hidden" name="_token" value="{{ csrf_token() }}">

		<div class="form-group">
					<div class="row">	

						<label class="col-sm-offset-1 control-label col-sm-1" >Vendedor</label>
							<input  class="col-sm-2" type="text" name="nombre" value= "{{$vendedorAnterior}}">
								

					</div>
					<br>
					<div class="row">	
						<a href="{{app()->make('urls')->getUrlAgregarUsuario()}}" class="col-sm-2 col-sm-offset-2 btn btn-primary">Agregar Nuevo Vendedor</a> 	
						<button type="submit" class="col-sm-1 col-sm-offset-5 btn btn-primary">Buscar</button>
           						
						
					</div>
		</div>	
		
</form></h4>

     <?php If (count($usuarios) == 0){?>   
		<div class="alert alert-warning">
			<strong>No existen Vendedores! </strong>Revise los filtros seleccionados.
		</div>
	  <?php } else {?>

<hr width=75%"/>
<div class="container">
  <h2>Vendedores</h2>
  <table class="table table-striped">
    <thead>
      <tr>
        <th>Nombre</th>
        <th>Mail</th>
        <th>Teléfono</th>
        <th>Acción</th>
      </tr>
    </thead>
    <tbody>
		
	 @foreach($usuarios as $usuario)
	<tr>
	
	<td>
		{{$usuario->nombre}}
	</td>
	
	<td>
		{{$usuario->email}}
	</td>
	<td>
		{{$usuario->telefono}}
	</td>
	<td> 
		<a href="{{app()->make('urls')->getUrlEditarUsuario($usuario->id)}}" class="btn-xs btn-primary">Editar</a>                                
	</td>
	
	</tr>
	@endforeach
	
    </tbody>
  </table>
</div>
    
    
     <?php } ?>

    </section>
	
      </section>
@endsection

