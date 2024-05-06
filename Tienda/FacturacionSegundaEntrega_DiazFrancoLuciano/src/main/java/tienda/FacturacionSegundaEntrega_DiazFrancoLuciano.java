package tienda;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.InputMismatchException;
import java.util.List;

import java.util.Scanner;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import tienda.modelos.Cliente;
import tienda.modelos.Producto;
import tienda.repositorios.ClienteRepository;
import tienda.repositorios.ProductoRepository;


@SpringBootApplication
public class FacturacionSegundaEntrega_DiazFrancoLuciano implements CommandLineRunner{

	@Autowired
	private ClienteRepository clienteRepository; // llamo al repositorio asi utilizamos todos sus metodos
	
	@Autowired
	private ProductoRepository productoRepository;// llamo al repositorio asi utilizamos todos sus metodos
	
	
	public static void main(String[] args) {
		SpringApplication.run(FacturacionSegundaEntrega_DiazFrancoLuciano.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		mostrarMenu();
		
	}
	
	public void mostrarMenu() {
		
		try {
			Scanner scanner = new Scanner(System.in);
			
			int opcion = -1; //Inicializo con una Opcion Invalida
			do {
				try {
					System.out.println("\n "+
							"****Bienvenidos a nuestra tienda de Ropa****\n"
							+ "\n "
							+ "Seleccione la operacion \n"
							+ "\n"+"*CLIENTES* \n "
							+ "1. Listar a todos los Clientes\n "
							+ "2. Buscar Cliente por DNI\n "
							+ "3. Agregar Cliente\n "
							+ "4. Modificar Cliente por DNI\n "
							+ "5. Eliminar Cliente por DNI\n "
							+ "\n"+"*PRODUCTOS* \n "
							+ "6. Listar todos los Productos\n "
							+ "7. Listar Productos Disponibles\n "
							+ "8. Buscar Producto por ID\n "
							+ "9. Agregar Producto\n "
							+ "10. Modificar Producto por ID\n "
							+ "11. Eliminar Producto por ID\n "
							+ "\n"+"*VENTA* \n "
							+ "12. Comprobante de Venta por DNI  NO DISPONIBLE\n "
							+ "\n "
							+ "0. Salir\n ");
					System.out.println("Ingresar opcion: ");
					
					//corroboramos que sea un numero que ingrasa
					if(scanner.hasNextInt()) {
						opcion = scanner.nextInt();
						scanner.nextLine();
					} else {
						System.out.println("Entrada Invalida. "
								+ "Debe Ingresar un Numero del Menu");
						scanner.nextLine();
						continue;
					}
					switch (opcion) {
					case 1:
						listaTodosLosClientes();
						break;
					case 2:
						buscarClientePorDNI(); 
						break;
					case 3:
						agregarCliente();
						break;
					case 4:
						modificarClientePorDNI();
						break;
					case 5:
						eliminarClientePorDNI();
						break;
					case 6:
						listarTodosLosProducto();
						break;
					case 7:
						listaProductosDisponibles();
						break;
					case 8:
						BuscarProductoPorId();
						break;
					case 9:
						agregarProducto();
						break;
					case 10:
						modificarProductoPorId();
						break;
					case 11:
						eliminarProductoPorId();
						break;
					case 12:
						comprobanteDeVentaPorDNI();
						break;
						
					case 0:
						System.out.println("Cerrando Programa.....\n"+
											"Programa Cerrado Exitosamente");
						break;
					default:
						System.out.println("Opcion invalida. Ingrese un Numero Valido.");
						break;
					}
		
				}catch(InputMismatchException e) {
					System.err.println("Error: Ingrese un Numero Valido.");
					scanner.nextLine();
					opcion = -1;
				}
								
			}while(opcion !=0);
			
			//cerramos el scanner
			scanner.close();
			
		}catch(Exception e) {
			e.getMessage();
		}
	}

	//METODOS CLIENTES
	public void listaTodosLosClientes() {
		List<Cliente> listaCliente = clienteRepository.findAll();// llamo a todos los clientes
		if (listaCliente.isEmpty()) {//si no existen clientes
			System.out.println("No existe Cliente para Mostrar!");
		}else {
			System.out.println("Lista de Cliente");
			for(Cliente cliente : listaCliente) {//recorre la lista de clientes y los trae
				System.out.println("El Cliente con DNI: #"
							+cliente.getDni()
							+" se llama "+cliente.getNombre()+" "
							+cliente.getApellido()
							+" y su numero de orden es "+cliente.getOrden());
			}
		}	
		
	}
	
	public void buscarClientePorDNI() {

		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el DNI del Cliente a Buscar:");
		int dni = scanner.nextInt();
		
		//buscamos al cliente de la tabla cliente por"finById"DNI
		Cliente cliente= clienteRepository.findById(dni).orElse(null);
		
		//Por si no existe el cliente buscado
		if(cliente!=null) {
			System.out.println("El Cliente Seleccionado es: "+cliente.getNombre()
					+" "+cliente.getApellido()
					+" y su numero de orden es "+cliente.getOrden());
		}else {
			System.out.println("El cliente con DNI: "+dni+" No fue encontrado");
		}
		
	}
	
	public void agregarCliente() {
		List<Producto> producto = productoRepository.findAll();
		if(producto.isEmpty()) {
			System.out.println("No existen Productos para mostrar. "
					+ "Debe agregar al menos un Producto!");
		}
		
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		Cliente cliente = new Cliente();//llamamos a un cliente nuevo
		
		System.out.println("Ingrese el DNI del Cliente:");
		cliente.setDni(scanner.nextInt());//utilizamos el scanner para que ingrese solo un entero
		scanner.nextLine();//para que haga un salto de linea
		
		System.out.println("Ingrese el Nombre:");
		cliente.setNombre(scanner.nextLine());
		
		System.out.println("Ingrese el Apellido:");
		cliente.setApellido(scanner.nextLine());
		
		System.out.println("Productos Disponibles: ");
		for (Producto productos: producto) {//
			System.out.println(" "+ productos.getId_producto() +". "+productos.getDescripcion());
		}
		
		//asignacion del producto
		int productoId;
		Producto productoSeleccionado = null;
		boolean productoValido = false;
		
		while(!productoValido) {// si existe el prodcuto
			try {
			
			System.out.println("Seleccione el ID del Producto: ");
			productoId = scanner.nextInt();
			productoSeleccionado = productoRepository.findById(productoId).orElse(null);
			if(productoSeleccionado != null) {//cuando
				productoValido=true;
			}else {
				System.out.println("El ID del Producto seleccionado no es Valido");
			}
			}catch(InputMismatchException e){// por si no ingresa un numero valido
				System.err.println("Error: Ingrese un Numero Valido.");
				scanner.nextLine();
			}
		}
		
		//Asignamos el Producto seleccionado
		
		
		cliente.setOrden((int) (Math.random() * 10000));
		
		clienteRepository.save(cliente);//guardamos al cliente
		
		System.out.println("Cliente guardado "+cliente.getNombre()+" "+
						cliente.getApellido()
						);
		
	}

	public void modificarClientePorDNI() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el DNI del Cliente a editar:");	
		int dni = scanner.nextInt();
		Cliente cliente= clienteRepository.findById(dni).orElse(null);
		List<Producto> producto = productoRepository.findAll();
		if(producto.isEmpty()) {
			System.out.println("No existen Productos para mostrar. "
					+ "Debe agregar al menos un Producto!");
		}
		//por si no existe el cliente
		if(cliente!=null) {
			System.out.println("El Cliente encontrado es: "+cliente.getNombre()+""
					+cliente.getApellido());
			System.out.println("Ingrese el nuevo Nombre: ");
			String nuevoNombre = scanner.next();
			cliente.setNombre(nuevoNombre);//asignamos el nuevo nombre
			System.out.println("Ingrese el nuevo Apellido: ");
			String nuevoApellido = scanner.next();
			cliente.setApellido(nuevoApellido);//asignamos el nuevo apellido
			
			System.out.println("Productos Disponibles: ");
			
			for(Producto productos : producto) {
				System.out.println(" "+productos.getId_producto()+"."+productos.getDescripcion());
			}
			
			int productoId;
			Producto productoSeleccionado = null;
			boolean productoValido = false;
			
			while(!productoValido) {
				try {
					
					System.out.println("Seleccione el ID del Producto: ");
					productoId = scanner.nextInt();
					//buscamos el id en el repository
					productoSeleccionado = productoRepository.findById(productoId).orElse(null);
					
					//si existe cambia a true, si no vuelve a pedir un dato
					if(productoSeleccionado != null) {
						productoValido = true;
					}else {
						System.out.println("El ID del Producto seleccionado no es Valido");
					}
				}catch(InputMismatchException e) {
					System.err.println("Error: Ingrese un Numero Valido para el Producto");
					scanner.nextLine();
				}
			}
			
			
			clienteRepository.save(cliente);// y lo guardamos
			System.out.println("Cliente modificado Correctamente");
			
		}else {
			System.out.println("Cliente con DNI " + dni + " no fue encontrado!");
		}
	}

	public void eliminarClientePorDNI() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el DNI del Cliente a editar:");		
		int dni = scanner.nextInt();
		Cliente cliente= clienteRepository.findById(dni).orElse(null);
		
		if(cliente !=null) {
			clienteRepository.delete(cliente);
			System.out.println("El Cliente fue Eliminado con Exito!");		
		}else {
			System.out.println("Cliente con DNI " + dni + " no fue encontrado!");		
		}	
	}

	
	//METODOS PRODUCTOS
	public void listarTodosLosProducto() {
		List<Producto>listaProducto = productoRepository.findAll();//llamo a todos los productos
		if (listaProducto.isEmpty()) {//si no existen productos
			System.out.println("No existe Producto para Mostrar!");
		}else {
			System.out.println("Lista de Productos");
			for(Producto producto : listaProducto) {
				System.out.println("Producto con ID: #_"
						+producto.getId_producto()
						+" "+ producto.getTipo()+", "
						+" "+producto.getDescripcion()
						+" y su valor es "+producto.getValor()+ "$");
			}
		}
	}
	
	public void listaProductosDisponibles() {
		List<Producto>listaProducto = productoRepository.findByVentasIsNull();//llamo a todos los productos
		if (listaProducto.isEmpty()) {//si no existen productos
			System.out.println("No existe Productos diponibles para Mostrar!");
		}else {
			System.out.println("Lista de Productos disponibles");
			for(Producto producto : listaProducto) {
				System.out.println("Producto con ID: #_"
						+producto.getId_producto()
						+" "+ producto.getTipo()+", "
						+" "+producto.getDescripcion()
						+" y su valor es "+producto.getValor()+ "$");
			}
		}
		
	}
	
	public void BuscarProductoPorId() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el Id del Producto a Buscar:");
		int id = scanner.nextInt(); 
		
		Producto producto = productoRepository.findById(id).orElse(null);
		
		//verificamos que exista el producto 
		if(producto !=null) {
			System.out.println("El Producto seleccionado es: "+producto.getTipo()
						+" "+producto.getDescripcion()
						+" y su valor es "+producto.getValor()+ "$");
		}else {
			System.out.println("El Producto con ID: "+id+" No fue encontrado");
		}
	}
	
  	public void agregarProducto() {
		Producto producto = new Producto();
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el Tipo de Producto:");
		producto.setTipo(scanner.nextLine());
		
		System.out.println("Ingrese la Descripcion del Producto:");
		producto.setDescripcion(scanner.nextLine());
		
		System.out.println("Ingrese el Valor del Producto:");
		String entrada = scanner.nextLine();
		try {
		    Integer valor = Integer.valueOf(entrada);//convertimos la cadena en entero
		    producto.setValor(valor);
		} catch (NumberFormatException e) {
		    System.out.println("Error: Ingresa un número entero válido.");
		}
		
		productoRepository.save(producto);
		System.out.println("El producto "+producto.getTipo()
							+" fue Guardado con Exito!.");
	}
	
  	public void modificarProductoPorId() {
  		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el ID del Producto a editar:");	
		int id = scanner.nextInt();
		
		Producto producto = productoRepository.findById(id).orElse(null);
		List<Cliente> cliente = clienteRepository.findAll();
		if(cliente.isEmpty()) {
			System.out.println("No existen Productos para mostrar. "
					+ "Debe agregar al menos un Producto!");
		}
		//por si no existe el producto
		if(producto!=null) {
			System.out.println("El Producto encontrado es: "+producto.getId_producto()+"#_ "+producto.getTipo()+" "
						+producto.getDescripcion() 
						+" y su valor es "+producto.getValor());
			System.out.println("Ingrese el nuevo Producto: ");
			String nuevoProducto = scanner.next();
			producto.setTipo(nuevoProducto);//asignamos el nuevo producto
			
			System.out.println("Ingrese la nueva Descripcion: ");
			String nuevaDescripcion = scanner.next();
			producto.setDescripcion(nuevaDescripcion);//asignamos el nuevo apellido
			
			// Consumir el salto de línea restante
			scanner.nextLine();
			
			System.out.println("Ingrese el Valor del Producto:");
			String valor = scanner.nextLine();
			try {
			    Integer nuevoValor = Integer.valueOf(valor);//convertimos la cadena en entero
			    producto.setValor(nuevoValor);
			} catch (NumberFormatException e) {
			    System.out.println("Error: Ingresa un número entero válido.");
			}
			
			productoRepository.save(producto);// y lo guardamos
			System.out.println("Producto modificado Correctamente");
			
		}else {
			System.out.println("Producto con ID" + id + " no fue encontrado!");
		}
	}
		
  	public void eliminarProductoPorId() {
		@SuppressWarnings("resource")
		Scanner scanner = new Scanner(System.in);
		System.out.println("Ingrese el ID del Producto a Eliminar:");		
		int id = scanner.nextInt();
		Producto producto= productoRepository.findById(id).orElse(null);
		
		if(producto !=null) {
			productoRepository.delete(producto);
			System.out.println("El Producto fue Eliminado con Exito!");		
		}else {
			System.out.println("producto con ID " + id + " no fue encontrado!");		
		}	
	}
  	
  	
  	//Metodo a realizar
  	public void comprobanteDeVentaPorDNI() {
  		//en la creacion de la venta preguntamos si queremos generar comprobante
  		//si es si, llamamos al metodo generarComprobante y mostramos en consola el comprobante
  		//si es no, llamamos al metodo generarComprobante y no lo mostramos en consola
  		//armar toda la logica del comprobante
  		LocalDateTime fechaEmisionComprobante = getCurrentDateTime();
  		//seteamos la fecha en el objeto comprobante
  		//save comprobante.
  	}
  	
  	@SuppressWarnings("deprecation")
  	private LocalDateTime getCurrentDateTime() {
		LocalDateTime currentDateTime = null;
		try {
            
			URL url = new URL("http://worldclockapi.com/api/json/utc/now");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();

            int respuesta = conn.getResponseCode();
            if (respuesta != 200) {
                throw new RuntimeException("Ocurrio un error! " + respuesta);
            } else {
                BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                String inputLine;
                StringBuffer content = new StringBuffer();
                while ((inputLine = in.readLine()) != null) {
                    content.append(inputLine);
                }
                in.close();

                // Parsear el JSON
                JsonObject jsonObject = JsonParser.parseString(content.toString()).getAsJsonObject();
                String currentDateTimeStr = jsonObject.get("currentDateTime").getAsString();

                // Convertir la fecha a ZonedDateTime
                ZonedDateTime dateTime = ZonedDateTime.parse(currentDateTimeStr);

                // Ajustar la zona horaria a UTC-3
                currentDateTime = dateTime.withZoneSameInstant(ZoneId.of("UTC-3")).toLocalDateTime();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
		return currentDateTime;
	}
  	
  	
  	}
	
	
	
	
	
	
	
	

