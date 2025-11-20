package teoremadelsabor.gui;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import teoremadelsabor.mvc.*;
import teoremadelsabor.strategy.*;
import teoremadelsabor.api.ServicioClima;
import teoremadelsabor.persistencia.GeneradorReportes;
import java.io.IOException;

/**
 * Ventana principal con diseño moderno estilo restaurante
 * Demuestra uso de Java Swing para puntos extra
 */
public class VentanaPrincipal extends JFrame {

  // Paleta de colores UNAM (Azul y Oro)
  private static final Color COLOR_PRINCIPAL = new Color(0, 63, 135);    // Azul UNAM
  private static final Color COLOR_SECUNDARIO = new Color(247, 179, 43); // Oro UNAM
  private static final Color COLOR_ACENTO = new Color(0, 82, 163);       // Azul más claro
  private static final Color COLOR_FONDO = new Color(245, 245, 245);     // Gris claro
  private static final Color COLOR_TEXTO = new Color(44, 62, 80);        // Gris oscuro
  private static final Color COLOR_BLANCO = new Color(255, 255, 255);
  private static final Color COLOR_HOVER = new Color(255, 200, 87);      // Oro brillante

  private ControladorPuestos controlador;
  private JTable tablaPuestos;
  private DefaultTableModel modeloTabla;
  private JTextArea areaDetalles;
  private JLabel labelClima;
  private JComboBox<String> comboFiltroTipo;
  private JComboBox<String> comboFiltroUbicacion;
  private JTextField txtFiltroPrecio;

  /**
   * Constructor de la ventana principal
   */
  public VentanaPrincipal(ControladorPuestos controlador) {
    this.controlador = controlador;

    setTitle("Teorema del Sabor - Sistema de Puestos");
    setSize(1200, 750);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLocationRelativeTo(null);
    getContentPane().setBackground(COLOR_FONDO);

    inicializarComponentes();
    cargarDatos();
    actualizarClima();
  }

  /**
   * Inicializa todos los componentes de la GUI
   */
  private void inicializarComponentes() {
    setLayout(new BorderLayout(0, 0));

    // Panel superior: Header con titulo y clima
    add(crearHeader(), BorderLayout.NORTH);

    // Panel central: Contenido principal
    add(crearContenidoPrincipal(), BorderLayout.CENTER);

    // Panel inferior: Filtros
    add(crearPanelFiltros(), BorderLayout.SOUTH);
  }

  /**
   * Crea el header superior con gradiente
   */
  private JPanel crearHeader() {
    JPanel header = new JPanel() {
      @Override
      protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        GradientPaint gp = new GradientPaint(0, 0, COLOR_PRINCIPAL, getWidth(), 0, COLOR_SECUNDARIO);
        g2d.setPaint(gp);
        g2d.fillRect(0, 0, getWidth(), getHeight());
      }
    };
    header.setLayout(new BorderLayout(20, 10));
    header.setPreferredSize(new Dimension(0, 120));
    header.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

    // Panel de titulo
    JPanel panelTitulo = new JPanel(new BorderLayout(10, 5));
    panelTitulo.setOpaque(false);

    JLabel titulo = new JLabel("\uD83C\uDF74 Teorema del Sabor");
    titulo.setFont(new Font("Dialog", Font.BOLD, 36));
    titulo.setForeground(COLOR_BLANCO);

    JLabel subtitulo = new JLabel("Sistema de Puestos de Comida - Facultad de Ciencias");
    subtitulo.setFont(new Font("Dialog", Font.PLAIN, 16));
    subtitulo.setForeground(new Color(255, 255, 255, 200));

    panelTitulo.add(titulo, BorderLayout.NORTH);
    panelTitulo.add(subtitulo, BorderLayout.CENTER);

    // Panel de clima
    JPanel panelClima = new JPanel(new BorderLayout(5, 5));
    panelClima.setOpaque(false);
    panelClima.setBorder(BorderFactory.createEmptyBorder(10, 0, 0, 0));

    labelClima = new JLabel("\u2601\uFE0F Cargando clima...");
    labelClima.setFont(new Font("Dialog", Font.PLAIN, 14));
    labelClima.setForeground(COLOR_BLANCO);
    labelClima.setHorizontalAlignment(JLabel.CENTER);

    panelClima.add(labelClima, BorderLayout.CENTER);

    header.add(panelTitulo, BorderLayout.WEST);
    header.add(panelClima, BorderLayout.SOUTH);

    return header;
  }

  /**
   * Crea el contenido principal con tabla y panel lateral
   */
  private JPanel crearContenidoPrincipal() {
    JPanel contenedor = new JPanel(new BorderLayout(15, 0));
    contenedor.setBackground(COLOR_FONDO);
    contenedor.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

    // Panel central con tabla
    contenedor.add(crearPanelTabla(), BorderLayout.CENTER);

    // Panel lateral derecho
    contenedor.add(crearPanelLateral(), BorderLayout.EAST);

    return contenedor;
  }

  /**
   * Crea el panel con la tabla estilizada
   */
  private JPanel crearPanelTabla() {
    JPanel panel = new JPanel(new BorderLayout(0, 10));
    panel.setBackground(COLOR_FONDO);

    // Titulo de seccion
    JLabel labelSeccion = new JLabel("\uD83D\uDCCB Puestos Disponibles");
    labelSeccion.setFont(new Font("Dialog", Font.BOLD, 20));
    labelSeccion.setForeground(COLOR_TEXTO);
    labelSeccion.setBorder(BorderFactory.createEmptyBorder(0, 5, 10, 0));
    panel.add(labelSeccion, BorderLayout.NORTH);

    // Crear tabla con estilo moderno
    String[] columnas = {"ID", "Nombre", "Tipo", "Ubicación", "Precio", "Estado"};
    modeloTabla = new DefaultTableModel(columnas, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    tablaPuestos = new JTable(modeloTabla);
    tablaPuestos.setFont(new Font("Segoe UI", Font.PLAIN, 13));
    tablaPuestos.setRowHeight(35);
    tablaPuestos.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    tablaPuestos.setShowVerticalLines(false);
    tablaPuestos.setIntercellSpacing(new Dimension(0, 0));
    tablaPuestos.setSelectionBackground(new Color(COLOR_PRINCIPAL.getRed(), COLOR_PRINCIPAL.getGreen(), COLOR_PRINCIPAL.getBlue(), 50));
    tablaPuestos.setSelectionForeground(COLOR_TEXTO);

    // Estilizar header
    JTableHeader header = tablaPuestos.getTableHeader();
    header.setFont(new Font("Dialog", Font.BOLD, 13));
    header.setBackground(COLOR_PRINCIPAL);
    header.setForeground(COLOR_BLANCO);
    header.setPreferredSize(new Dimension(0, 40));
    header.setBorder(BorderFactory.createEmptyBorder());

    // Renderizador personalizado para celdas
    DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
    centerRenderer.setHorizontalAlignment(JLabel.CENTER);
    tablaPuestos.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
    tablaPuestos.getColumnModel().getColumn(4).setCellRenderer(centerRenderer);
    tablaPuestos.getColumnModel().getColumn(5).setCellRenderer(centerRenderer);

    // Ajustar anchos de columnas
    tablaPuestos.getColumnModel().getColumn(0).setPreferredWidth(50);
    tablaPuestos.getColumnModel().getColumn(1).setPreferredWidth(180);
    tablaPuestos.getColumnModel().getColumn(2).setPreferredWidth(130);
    tablaPuestos.getColumnModel().getColumn(3).setPreferredWidth(130);
    tablaPuestos.getColumnModel().getColumn(4).setPreferredWidth(80);
    tablaPuestos.getColumnModel().getColumn(5).setPreferredWidth(100);

    // Listener para seleccion
    tablaPuestos.getSelectionModel().addListSelectionListener(e -> {
      if (!e.getValueIsAdjusting()) {
        mostrarDetallesPuesto();
      }
    });

    JScrollPane scrollPane = new JScrollPane(tablaPuestos);
    scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 1));
    scrollPane.getViewport().setBackground(COLOR_BLANCO);
    panel.add(scrollPane, BorderLayout.CENTER);

    return panel;
  }

  /**
   * Crea el panel lateral con detalles y acciones
   */
  private JPanel crearPanelLateral() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setPreferredSize(new Dimension(400, 0));
    panel.setBackground(COLOR_FONDO);

    // Panel de detalles
    JPanel panelDetalles = crearPanelDetalles();
    panel.add(panelDetalles);

    panel.add(Box.createRigidArea(new Dimension(0, 15)));

    // Panel de acciones
    JPanel panelAcciones = crearPanelAcciones();
    panel.add(panelAcciones);

    return panel;
  }

  /**
   * Crea el panel de detalles del puesto
   */
  private JPanel crearPanelDetalles() {
    JPanel panel = new JPanel(new BorderLayout(0, 10));
    panel.setBackground(COLOR_BLANCO);
    panel.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
      BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));
    panel.setMaximumSize(new Dimension(400, 400));

    JLabel titulo = new JLabel("\uD83D\uDCCC Detalles del Puesto");
    titulo.setFont(new Font("Dialog", Font.BOLD, 16));
    titulo.setForeground(COLOR_TEXTO);
    panel.add(titulo, BorderLayout.NORTH);

    areaDetalles = new JTextArea();
    areaDetalles.setEditable(false);
    areaDetalles.setFont(new Font("Dialog", Font.PLAIN, 13));
    areaDetalles.setLineWrap(true);
    areaDetalles.setWrapStyleWord(true);
    areaDetalles.setMargin(new Insets(10, 10, 10, 10));
    areaDetalles.setBackground(new Color(250, 250, 250));
    areaDetalles.setForeground(COLOR_TEXTO);
    areaDetalles.setText("Selecciona un puesto de la tabla\npara ver sus detalles...");

    JScrollPane scrollDetalles = new JScrollPane(areaDetalles);
    scrollDetalles.setBorder(BorderFactory.createLineBorder(new Color(230, 230, 230), 1));
    panel.add(scrollDetalles, BorderLayout.CENTER);

    return panel;
  }

  /**
   * Crea el panel de acciones con botones estilizados
   */
  private JPanel crearPanelAcciones() {
    JPanel panel = new JPanel();
    panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
    panel.setBackground(COLOR_BLANCO);
    panel.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createLineBorder(new Color(220, 220, 220), 1),
      BorderFactory.createEmptyBorder(15, 15, 15, 15)
    ));
    panel.setMaximumSize(new Dimension(400, 500));

    JLabel titulo = new JLabel("\u26A1 Acciones Rápidas");
    titulo.setFont(new Font("Dialog", Font.BOLD, 16));
    titulo.setForeground(COLOR_TEXTO);
    titulo.setAlignmentX(Component.LEFT_ALIGNMENT);
    panel.add(titulo);
    panel.add(Box.createRigidArea(new Dimension(0, 15)));

    // Botones con iconos
    panel.add(crearBotonEstilizado("\uD83D\uDD14 Suscribirse", COLOR_ACENTO, e -> suscribirUsuario()));
    panel.add(Box.createRigidArea(new Dimension(0, 10)));

    panel.add(crearBotonEstilizado("\uD83D\uDCC4 Reporte TXT", COLOR_SECUNDARIO, e -> generarReporte("TXT")));
    panel.add(Box.createRigidArea(new Dimension(0, 10)));

    panel.add(crearBotonEstilizado("\uD83D\uDCCA Reporte CSV", COLOR_SECUNDARIO, e -> generarReporte("CSV")));
    panel.add(Box.createRigidArea(new Dimension(0, 10)));

    panel.add(crearBotonEstilizado("\u2601\uFE0F Actualizar Clima", COLOR_PRINCIPAL, e -> actualizarClima()));
    panel.add(Box.createRigidArea(new Dimension(0, 10)));

    panel.add(crearBotonEstilizado("\uD83D\uDD04 Refrescar Lista", COLOR_PRINCIPAL, e -> cargarDatos()));
    panel.add(Box.createRigidArea(new Dimension(0, 10)));

    panel.add(crearBotonEstilizado("\u274C Salir", new Color(231, 76, 60), e -> {
      int opcion = JOptionPane.showConfirmDialog(
        this,
        "¿Seguro que deseas salir?",
        "Confirmar Salida",
        JOptionPane.YES_NO_OPTION
      );
      if (opcion == JOptionPane.YES_OPTION) {
        System.exit(0);
      }
    }));

    return panel;
  }

  /**
   * Crea un boton estilizado moderno
   */
  private JButton crearBotonEstilizado(String texto, Color color, ActionListener listener) {
    JButton boton = new JButton(texto);
    boton.setFont(new Font("Dialog", Font.BOLD, 13));
    boton.setForeground(COLOR_BLANCO);
    boton.setBackground(color);
    boton.setBorderPainted(false);
    boton.setFocusPainted(false);
    boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    boton.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
    boton.setAlignmentX(Component.LEFT_ALIGNMENT);
    boton.addActionListener(listener);

    // Efecto hover
    boton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        boton.setBackground(color.brighter());
      }

      @Override
      public void mouseExited(MouseEvent e) {
        boton.setBackground(color);
      }
    });

    return boton;
  }

  /**
   * Crea el panel de filtros en la parte inferior
   */
  private JPanel crearPanelFiltros() {
    JPanel panel = new JPanel();
    panel.setLayout(new FlowLayout(FlowLayout.LEFT, 15, 15));
    panel.setBackground(COLOR_BLANCO);
    panel.setBorder(BorderFactory.createCompoundBorder(
      BorderFactory.createMatteBorder(1, 0, 0, 0, new Color(220, 220, 220)),
      BorderFactory.createEmptyBorder(10, 20, 10, 20)
    ));

    JLabel labelFiltros = new JLabel("\uD83D\uDD0D Filtros:");
    labelFiltros.setFont(new Font("Dialog", Font.BOLD, 14));
    labelFiltros.setForeground(COLOR_TEXTO);
    panel.add(labelFiltros);

    // Filtro por tipo
    panel.add(new JLabel("Tipo:"));
    comboFiltroTipo = new JComboBox<>(new String[]{
      "Todos", "Comida Completa", "Comida Rapida", "Tacos", "Snacks", "Postres"
    });
    comboFiltroTipo.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    panel.add(comboFiltroTipo);

    // Filtro por ubicacion
    panel.add(new JLabel("Ubicación:"));
    comboFiltroUbicacion = new JComboBox<>(new String[]{
      "Todas", "Comedor", "Estacionamiento", "Media Luna"
    });
    comboFiltroUbicacion.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    panel.add(comboFiltroUbicacion);

    // Filtro por precio
    panel.add(new JLabel("Precio máximo:"));
    txtFiltroPrecio = new JTextField(10);
    txtFiltroPrecio.setFont(new Font("Segoe UI", Font.PLAIN, 12));
    panel.add(txtFiltroPrecio);

    // Botones de filtros
    JButton btnAplicar = crearBotonFiltro("Aplicar", COLOR_ACENTO);
    btnAplicar.addActionListener(e -> aplicarFiltros());
    panel.add(btnAplicar);

    JButton btnLimpiar = crearBotonFiltro("Limpiar", new Color(149, 165, 166));
    btnLimpiar.addActionListener(e -> {
      comboFiltroTipo.setSelectedIndex(0);
      comboFiltroUbicacion.setSelectedIndex(0);
      txtFiltroPrecio.setText("");
      cargarDatos();
    });
    panel.add(btnLimpiar);

    return panel;
  }

  /**
   * Crea un boton para filtros
   */
  private JButton crearBotonFiltro(String texto, Color color) {
    JButton boton = new JButton(texto);
    boton.setFont(new Font("Dialog", Font.BOLD, 12));
    boton.setForeground(COLOR_BLANCO);
    boton.setBackground(color);
    boton.setBorderPainted(false);
    boton.setFocusPainted(false);
    boton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    boton.setPreferredSize(new Dimension(90, 30));

    boton.addMouseListener(new MouseAdapter() {
      @Override
      public void mouseEntered(MouseEvent e) {
        boton.setBackground(color.brighter());
      }

      @Override
      public void mouseExited(MouseEvent e) {
        boton.setBackground(color);
      }
    });

    return boton;
  }

  /**
   * Carga los datos de puestos en la tabla
   */
  private void cargarDatos() {
    modeloTabla.setRowCount(0);
    List<PuestoComida> puestos = controlador.getGestor().getPuestos();

    for (PuestoComida puesto : puestos) {
      Object[] fila = {
        puesto.getId(),
        puesto.getNombre(),
        puesto.getTipo(),
        puesto.getUbicacion(),
        String.format("$%.1f", puesto.getPrecioPromedio()),
        puesto.getEstado().toString()
      };
      modeloTabla.addRow(fila);
    }
  }

  /**
   * Muestra detalles del puesto seleccionado
   */
  private void mostrarDetallesPuesto() {
    int fila = tablaPuestos.getSelectedRow();
    if (fila >= 0) {
      String id = (String) modeloTabla.getValueAt(fila, 0);
      PuestoComida puesto = controlador.getGestor().buscarPorId(id);

      if (puesto != null) {
        StringBuilder sb = new StringBuilder();
        sb.append("\uD83C\uDFEA ").append(puesto.getNombre()).append("\n\n");
        sb.append("\uD83C\uDD94 ID: ").append(puesto.getId()).append("\n");
        sb.append("\uD83C\uDF7D\uFE0F Tipo: ").append(puesto.getTipo()).append("\n");
        sb.append("\uD83D\uDCCD Ubicación: ").append(puesto.getUbicacion()).append("\n");
        sb.append("\uD83D\uDCB0 Precio: $").append(puesto.getPrecioPromedio()).append("\n");
        sb.append("\uD83D\uDD50 Horario: ").append(puesto.getHoraApertura())
          .append(" - ").append(puesto.getHoraCierre()).append("\n");
        sb.append("\uD83D\uDCB3 Pago: ").append(puesto.getMetodosPago()).append("\n");
        sb.append("\uD83D\uDD34 Estado: ").append(puesto.getEstado().toString()).append("\n");

        areaDetalles.setText(sb.toString());
      }
    }
  }

  /**
   * Aplica los filtros seleccionados
   */
  private void aplicarFiltros() {
    String tipo = (String) comboFiltroTipo.getSelectedItem();
    String ubicacion = (String) comboFiltroUbicacion.getSelectedItem();
    String precioTexto = txtFiltroPrecio.getText().trim();

    if (!tipo.equals("Todos")) {
      List<PuestoComida> filtrados = controlador.getGestor()
        .recomendar(new RecomendacionTipo(tipo));
      actualizarTablaConLista(filtrados);
      return;
    }

    if (!ubicacion.equals("Todas")) {
      List<PuestoComida> filtrados = controlador.getGestor()
        .recomendar(new RecomendacionUbicacion(ubicacion));
      actualizarTablaConLista(filtrados);
      return;
    }

    if (!precioTexto.isEmpty()) {
      try {
        double precio = Double.parseDouble(precioTexto);
        List<PuestoComida> filtrados = controlador.getGestor()
          .recomendar(new RecomendacionPrecio(precio));
        actualizarTablaConLista(filtrados);
        return;
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(
          this,
          "Precio inválido",
          "Error",
          JOptionPane.ERROR_MESSAGE
        );
      }
    }

    cargarDatos();
  }

  /**
   * Actualiza la tabla con una lista especifica
   */
  private void actualizarTablaConLista(List<PuestoComida> puestos) {
    modeloTabla.setRowCount(0);
    for (PuestoComida puesto : puestos) {
      Object[] fila = {
        puesto.getId(),
        puesto.getNombre(),
        puesto.getTipo(),
        puesto.getUbicacion(),
        String.format("$%.1f", puesto.getPrecioPromedio()),
        puesto.getEstado().toString()
      };
      modeloTabla.addRow(fila);
    }
  }

  /**
   * Suscribe un usuario al puesto seleccionado
   */
  private void suscribirUsuario() {
    int fila = tablaPuestos.getSelectedRow();
    if (fila < 0) {
      JOptionPane.showMessageDialog(
        this,
        "Por favor, selecciona un puesto de la tabla",
        "Aviso",
        JOptionPane.WARNING_MESSAGE
      );
      return;
    }

    String nombre = JOptionPane.showInputDialog(
      this,
      "Ingresa tu nombre:",
      "Suscripción a Notificaciones",
      JOptionPane.QUESTION_MESSAGE
    );

    if (nombre != null && !nombre.trim().isEmpty()) {
      String id = (String) modeloTabla.getValueAt(fila, 0);
      if (controlador.suscribirUsuario(nombre, id)) {
        JOptionPane.showMessageDialog(
          this,
          "¡Suscripción exitosa!\nRecibirás notificaciones de cambios en el puesto.",
          "Éxito",
          JOptionPane.INFORMATION_MESSAGE
        );
      } else {
        JOptionPane.showMessageDialog(
          this,
          "Error al procesar la suscripción",
          "Error",
          JOptionPane.ERROR_MESSAGE
        );
      }
    }
  }

  /**
   * Genera un reporte en el formato especificado
   */
  private void generarReporte(String formato) {
    try {
      if (formato.equals("TXT")) {
        GeneradorReportes.generarReporteTXT(
          controlador.getGestor().getPuestos(),
          "reporte_puestos.txt"
        );
        JOptionPane.showMessageDialog(
          this,
          "✓ Reporte generado exitosamente:\nreporte_puestos.txt",
          "Éxito",
          JOptionPane.INFORMATION_MESSAGE
        );
      } else if (formato.equals("CSV")) {
        GeneradorReportes.generarReporteCSV(
          controlador.getGestor().getPuestos(),
          "reporte_puestos.csv"
        );
        JOptionPane.showMessageDialog(
          this,
          "✓ Reporte generado exitosamente:\nreporte_puestos.csv",
          "Éxito",
          JOptionPane.INFORMATION_MESSAGE
        );
      }

      GeneradorReportes.escribirLog(
        "Reporte " + formato + " generado desde GUI",
        "actividad.log"
      );

    } catch (IOException e) {
      JOptionPane.showMessageDialog(
        this,
        "Error al generar el reporte:\n" + e.getMessage(),
        "Error",
        JOptionPane.ERROR_MESSAGE
      );
    }
  }

  /**
   * Actualiza el clima desde la API de forma asíncrona
   */
  private void actualizarClima() {
    labelClima.setText("\u2601\uFE0F Consultando API del clima...");

    SwingWorker<ServicioClima.ClimaInfo, Void> worker = new SwingWorker<ServicioClima.ClimaInfo, Void>() {
      @Override
      protected ServicioClima.ClimaInfo doInBackground() {
        // Esta llamada ya usa el cache implementado en ServicioClima
        return ServicioClima.obtenerClimaInfo();
      }

      @Override
      protected void done() {
        try {
          ServicioClima.ClimaInfo resultado = get();
          labelClima.setText(resultado.toSimpleString());
        } catch (Exception e) {
          labelClima.setText("\u2601\uFE0F Clima no disponible. Revisa la conexión.");
          e.printStackTrace();
        }
      }
    };

    worker.execute();
  }
}
