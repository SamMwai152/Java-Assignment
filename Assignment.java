import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import java.text.*;
import java.util.*;
import java.util.List;

public class RestaurantPOS extends JFrame {

    // ─── Color Palette ───────────────────────────────────────────────────────
    private static final Color BG_DARK       = new Color(18, 18, 30);
    private static final Color BG_PANEL      = new Color(28, 28, 45);
    private static final Color BG_CARD       = new Color(38, 38, 58);
    private static final Color ACCENT_BLUE   = new Color(66, 135, 245);
    private static final Color ACCENT_GREEN  = new Color(52, 199, 89);
    private static final Color ACCENT_RED    = new Color(255, 69, 58);
    private static final Color ACCENT_ORANGE = new Color(255, 149, 0);
    private static final Color ACCENT_PURPLE = new Color(175, 82, 222);
    private static final Color TEXT_PRIMARY  = new Color(245, 245, 250);
    private static final Color TEXT_SECONDARY= new Color(160, 160, 180);
    private static final Color DIVIDER       = new Color(55, 55, 80);

    // ─── Category Colors ─────────────────────────────────────────────────────
    private static final Color[] CAT_COLORS = {
        new Color(220, 50, 50),   // Burgers  – red
        new Color(220, 120, 20),  // Sides    – orange
        new Color(180, 60, 200),  // Wine&Beer– purple
        new Color(30, 160, 80),   // Discounts– green
        new Color(200, 60, 80),   // Salads   – crimson
        new Color(40, 120, 220),  // Drinks   – blue
        new Color(200, 140, 20),  // Desserts – gold
        new Color(30, 170, 170),  // Scan     – teal
    };

    // ─── Data ─────────────────────────────────────────────────────────────────
    private final Map<String, List<MenuItem>> menuData = new LinkedHashMap<>();
    private final List<OrderItem> currentOrder = new ArrayList<>();
    private String currentCategory = "Burgers";
    private int tableNumber = 1;
    private int orderCounter = 14;

    // ─── UI refs ──────────────────────────────────────────────────────────────
    private JPanel menuItemsPanel;
    private JLabel tableLabel, orderNumLabel;
    private DefaultTableModel orderTableModel;
    private JLabel subtotalLabel, taxLabel, totalLabel;
    private JLabel statusBar;

    // ══════════════════════════════════════════════════════════════════════════
    public RestaurantPOS() {
        buildMenuData();
        setTitle("Omega Gardens Hotel – Restaurant POS");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setSize(1280, 780);
        setMinimumSize(new Dimension(1100, 680));
        setLocationRelativeTo(null);
        setBackground(BG_DARK);

        JPanel root = new JPanel(new BorderLayout(0, 0));
        root.setBackground(BG_DARK);
        root.add(buildTopBar(),   BorderLayout.NORTH);
        root.add(buildCenter(),   BorderLayout.CENTER);
        root.add(buildStatusBar(),BorderLayout.SOUTH);
        setContentPane(root);
        setVisible(true);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  MENU DATA
    // ══════════════════════════════════════════════════════════════════════════
    private void buildMenuData() {
        menuData.put("Burgers", Arrays.asList(
            new MenuItem("Cheeseburger",      11.00, "🍔"),
            new MenuItem("The Classic",       10.50, "🍔"),
            new MenuItem("Veggie Burger",      9.50, "🥦"),
            new MenuItem("Mushroom Burger",   10.00, "🍄"),
            new MenuItem("Paradiso Burger",   12.50, "🌶"),
            new MenuItem("Double Bacon",      13.00, "🥓"),
            new MenuItem("Little Bird",        9.00, "🐦"),
            new MenuItem("The Atomic",        14.00, "💥"),
            new MenuItem("Buffalo Burger",    11.50, "🐃"),
            new MenuItem("Texas Meatlovers",  13.50, "🤠"),
            new MenuItem("Irish Fries",        7.00, "🍟"),
            new MenuItem("Potato Skins",       6.50, "🥔"),
            new MenuItem("Mac & Cheese",       8.00, "🧀")
        ));
        menuData.put("Sides", Arrays.asList(
            new MenuItem("French Fries",       4.00, "🍟"),
            new MenuItem("Onion Rings",        4.50, "🧅"),
            new MenuItem("Coleslaw",           3.00, "🥗"),
            new MenuItem("Garlic Bread",       3.50, "🥖"),
            new MenuItem("Corn on the Cob",    4.00, "🌽"),
            new MenuItem("Sweet Potato Fries", 5.00, "🍠")
        ));
        menuData.put("Wine & Beer", Arrays.asList(
            new MenuItem("House Red Wine",    8.00, "🍷"),
            new MenuItem("House White Wine",  8.00, "🥂"),
            new MenuItem("Craft Beer",        6.00, "🍺"),
            new MenuItem("Heineken",          5.50, "🍻"),
            new MenuItem("Guinness",          6.50, "🍺"),
            new MenuItem("Rosé",             8.50, "🍷")
        ));
        menuData.put("Discounts", Arrays.asList(
            new MenuItem("Staff Discount 10%", -0.10, "🏷"),
            new MenuItem("Happy Hour 20%",    -0.20, "⏰"),
            new MenuItem("Loyalty -$2",       -2.00, "⭐"),
            new MenuItem("Manager Comp",      -5.00, "🎁")
        ));
        menuData.put("Salads", Arrays.asList(
            new MenuItem("Caesar Salad",       7.50, "🥗"),
            new MenuItem("Greek Salad",        8.00, "🫒"),
            new MenuItem("Garden Salad",       6.50, "🌱"),
            new MenuItem("Nicoise Salad",      9.00, "🐟"),
            new MenuItem("Caprese",            8.50, "🍅")
        ));
        menuData.put("Drinks", Arrays.asList(
            new MenuItem("Soda",               2.50, "🥤"),
            new MenuItem("Lemonade",           3.00, "🍋"),
            new MenuItem("Iced Tea",           2.50, "🧋"),
            new MenuItem("Orange Juice",       4.00, "🍊"),
            new MenuItem("Water",              1.50, "💧"),
            new MenuItem("Coffee",             3.50, "☕"),
            new MenuItem("Milkshake",          5.00, "🥛")
        ));
        menuData.put("Desserts", Arrays.asList(
            new MenuItem("Chocolate Cake",     6.50, "🍫"),
            new MenuItem("Ice Cream",          4.50, "🍦"),
            new MenuItem("Cheesecake",         7.00, "🍰"),
            new MenuItem("Brownie",            5.00, "🍫"),
            new MenuItem("Fruit Salad",        5.50, "🍓")
        ));
        menuData.put("Scan", Arrays.asList(
            new MenuItem("Custom Item",        0.00, "📦"),
            new MenuItem("Daily Special",      0.00, "⭐")
        ));
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  TOP BAR
    // ══════════════════════════════════════════════════════════════════════════
    private JPanel buildTopBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(BG_PANEL);
        bar.setBorder(new MatteBorder(0, 0, 1, 0, DIVIDER));
        bar.setPreferredSize(new Dimension(0, 56));

        // Left – logo
        JLabel logo = new JLabel("  🌿 Omega Gardens Hotel POS");
        logo.setFont(new Font("SansSerif", Font.BOLD, 16));
        logo.setForeground(TEXT_PRIMARY);

        // Center – table selector
        JPanel center = new JPanel(new FlowLayout(FlowLayout.CENTER, 12, 8));
        center.setOpaque(false);
        JLabel tLbl = new JLabel("Table:");
        tLbl.setForeground(TEXT_SECONDARY);
        tLbl.setFont(new Font("SansSerif", Font.PLAIN, 13));
        tableLabel = new JLabel("T" + tableNumber);
        tableLabel.setForeground(ACCENT_BLUE);
        tableLabel.setFont(new Font("SansSerif", Font.BOLD, 15));

        JButton prev = iconBtn("◀"), next = iconBtn("▶");
        prev.addActionListener(e -> changeTable(-1));
        next.addActionListener(e -> changeTable(+1));

        orderNumLabel = new JLabel("Order #" + orderCounter);
        orderNumLabel.setForeground(TEXT_SECONDARY);
        orderNumLabel.setFont(new Font("SansSerif", Font.PLAIN, 13));

        center.add(tLbl); center.add(prev); center.add(tableLabel); center.add(next);
        center.add(new JSeparator(JSeparator.VERTICAL));
        center.add(orderNumLabel);

        // Right – time + new order
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 12, 10));
        right.setOpaque(false);
        JLabel clock = new JLabel();
        clock.setForeground(TEXT_SECONDARY);
        clock.setFont(new Font("SansSerif", Font.PLAIN, 13));
        javax.swing.Timer t = new javax.swing.Timer(1000, e -> {
            clock.setText(new SimpleDateFormat("HH:mm:ss  dd MMM yyyy").format(new Date()));
        });
        t.start(); t.getActionListeners()[0].actionPerformed(null);

        JButton newOrder = roundBtn("＋ New Order", ACCENT_GREEN, Color.WHITE);
        newOrder.addActionListener(e -> newOrder());

        right.add(clock); right.add(newOrder);

        bar.add(logo, BorderLayout.WEST);
        bar.add(center, BorderLayout.CENTER);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  CENTER  (left = menu | right = check)
    // ══════════════════════════════════════════════════════════════════════════
    private JSplitPane buildCenter() {
        JSplitPane split = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                buildMenuPanel(), buildCheckPanel());
        split.setDividerLocation(740);
        split.setDividerSize(4);
        split.setBackground(DIVIDER);
        split.setBorder(null);
        return split;
    }

    // ─── LEFT: Category tabs + item grid ─────────────────────────────────────
    private JPanel buildMenuPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_DARK);

        // Search bar
        JPanel searchRow = new JPanel(new BorderLayout(8, 0));
        searchRow.setBackground(BG_DARK);
        searchRow.setBorder(new EmptyBorder(10, 12, 6, 12));
        JTextField search = new JTextField();
        styleTextField(search, "🔍  Search menu...");
        search.addActionListener(e -> filterMenu(search.getText()));
        search.getDocument().addDocumentListener(new javax.swing.event.DocumentListener() {
            public void insertUpdate(javax.swing.event.DocumentEvent e) { filterMenu(search.getText()); }
            public void removeUpdate(javax.swing.event.DocumentEvent e) { filterMenu(search.getText()); }
            public void changedUpdate(javax.swing.event.DocumentEvent e) {}
        });
        searchRow.add(search, BorderLayout.CENTER);

        // Category tabs
        JPanel catPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 8));
        catPanel.setBackground(BG_DARK);
        catPanel.setBorder(new EmptyBorder(0, 6, 0, 6));
        String[] categories = menuData.keySet().toArray(new String[0]);
        Color[] catColors = CAT_COLORS;
        for (int i = 0; i < categories.length; i++) {
            final String cat = categories[i];
            final Color col = catColors[i % catColors.length];
            JButton btn = buildCatButton(cat, col);
            btn.addActionListener(e -> selectCategory(cat, col));
            catPanel.add(btn);
        }

        // Items grid
        menuItemsPanel = new JPanel(new GridLayout(0, 3, 8, 8));
        menuItemsPanel.setBackground(BG_DARK);
        menuItemsPanel.setBorder(new EmptyBorder(4, 12, 12, 12));

        JScrollPane scroll = new JScrollPane(menuItemsPanel);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_DARK);
        scroll.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        panel.add(searchRow, BorderLayout.NORTH);
        JPanel top = new JPanel(new BorderLayout());
        top.setBackground(BG_DARK);
        top.add(searchRow, BorderLayout.NORTH);
        top.add(catPanel, BorderLayout.CENTER);
        panel.add(top, BorderLayout.NORTH);
        panel.add(scroll, BorderLayout.CENTER);

        loadCategory("Burgers");
        return panel;
    }

    private JButton buildCatButton(String name, Color color) {
        JButton btn = new JButton(name);
        btn.setFont(new Font("SansSerif", Font.BOLD, 12));
        btn.setForeground(name.equals(currentCategory) ? Color.WHITE : TEXT_PRIMARY);
        btn.setBackground(name.equals(currentCategory) ? color : BG_CARD);
        btn.setBorder(new EmptyBorder(7, 14, 7, 14));
        btn.setFocusPainted(false);
        btn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        btn.putClientProperty("color", color);
        btn.putClientProperty("name", name);
        return btn;
    }

    private void selectCategory(String cat, Color color) {
        currentCategory = cat;
        loadCategory(cat);
        refreshCategoryButtons();
    }

    private void refreshCategoryButtons() {
        // Walk the component tree to find the category panel and update button states
        refreshCategoryButtonsIn(getContentPane());
    }

    private void refreshCategoryButtonsIn(Container container) {
        for (Component c : container.getComponents()) {
            if (c instanceof JButton b) {
                String name = (String) b.getClientProperty("name");
                Color col   = (Color)  b.getClientProperty("color");
                if (name != null && col != null) {
                    b.setBackground(name.equals(currentCategory) ? col : BG_CARD);
                    b.setForeground(name.equals(currentCategory) ? Color.WHITE : TEXT_PRIMARY);
                    b.repaint();
                }
            }
            if (c instanceof Container sub) {
                refreshCategoryButtonsIn(sub);
            }
        }
    }

    private void loadCategory(String cat) {
        List<MenuItem> items = menuData.getOrDefault(cat, Collections.emptyList());
        renderItems(items);
    }

    private void filterMenu(String text) {
        if (text == null || text.isBlank()) {
            loadCategory(currentCategory);
            return;
        }
        String q = text.toLowerCase();
        List<MenuItem> filtered = new ArrayList<>();
        for (List<MenuItem> items : menuData.values())
            for (MenuItem m : items)
                if (m.name.toLowerCase().contains(q))
                    filtered.add(m);
        renderItems(filtered);
    }

    private void renderItems(List<MenuItem> items) {
        menuItemsPanel.removeAll();
        for (MenuItem item : items) {
            menuItemsPanel.add(buildItemCard(item));
        }
        menuItemsPanel.revalidate();
        menuItemsPanel.repaint();
    }

    private JPanel buildItemCard(MenuItem item) {
        JPanel card = new JPanel(new BorderLayout(4, 4));
        card.setBackground(BG_CARD);
        card.setBorder(new CompoundBorder(
            new LineBorder(DIVIDER, 1, true),
            new EmptyBorder(10, 10, 10, 10)
        ));
        card.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        JLabel emoji = new JLabel(item.emoji, SwingConstants.CENTER);
        emoji.setFont(new Font("SansSerif", Font.PLAIN, 28));

        JLabel name = new JLabel(item.name, SwingConstants.CENTER);
        name.setFont(new Font("SansSerif", Font.BOLD, 12));
        name.setForeground(TEXT_PRIMARY);

        String priceStr = item.price < 0 ? String.format("-$%.2f", Math.abs(item.price))
                                         : String.format("$%.2f", item.price);
        JLabel price = new JLabel(priceStr, SwingConstants.CENTER);
        price.setFont(new Font("SansSerif", Font.BOLD, 12));
        price.setForeground(item.price < 0 ? ACCENT_GREEN : ACCENT_BLUE);

        JPanel bottom = new JPanel(new BorderLayout());
        bottom.setOpaque(false);
        bottom.add(name, BorderLayout.CENTER);
        bottom.add(price, BorderLayout.SOUTH);

        card.add(emoji, BorderLayout.CENTER);
        card.add(bottom, BorderLayout.SOUTH);

        // Hover effect
        card.addMouseListener(new MouseAdapter() {
            public void mouseEntered(MouseEvent e) {
                card.setBackground(new Color(50, 50, 75));
                card.setBorder(new CompoundBorder(
                    new LineBorder(ACCENT_BLUE, 1, true),
                    new EmptyBorder(10, 10, 10, 10)));
            }
            public void mouseExited(MouseEvent e) {
                card.setBackground(BG_CARD);
                card.setBorder(new CompoundBorder(
                    new LineBorder(DIVIDER, 1, true),
                    new EmptyBorder(10, 10, 10, 10)));
            }
            public void mouseClicked(MouseEvent e) { addToOrder(item); }
        });

        return card;
    }

    // ─── RIGHT: Check panel ───────────────────────────────────────────────────
    private JPanel buildCheckPanel() {
        JPanel panel = new JPanel(new BorderLayout(0, 0));
        panel.setBackground(BG_PANEL);
        panel.setBorder(new MatteBorder(0, 1, 0, 0, DIVIDER));
        panel.setPreferredSize(new Dimension(380, 0));

        // Header tabs
        JPanel tabs = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        tabs.setBackground(BG_PANEL);
        tabs.setBorder(new MatteBorder(0, 0, 1, 0, DIVIDER));
        String[] tabNames = {"Check", "Actions", "Guest"};
        for (String t : tabNames) {
            JLabel tab = new JLabel(t);
            tab.setFont(new Font("SansSerif", t.equals("Check") ? Font.BOLD : Font.PLAIN, 13));
            tab.setForeground(t.equals("Check") ? ACCENT_BLUE : TEXT_SECONDARY);
            tab.setBorder(new EmptyBorder(12, 18, 12, 18));
            tabs.add(tab);
        }

        // Order table
        String[] cols = {"Item", "Qty", "Price"};
        orderTableModel = new DefaultTableModel(cols, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable table = new JTable(orderTableModel);
        styleOrderTable(table);

        JScrollPane scroll = new JScrollPane(table);
        scroll.setBorder(null);
        scroll.getViewport().setBackground(BG_PANEL);
        scroll.setBackground(BG_PANEL);

        // Totals
        JPanel totals = buildTotalsPanel();

        // Action buttons
        JPanel actions = buildActionButtons();

        // Bottom nav
        JPanel nav = buildBottomNav();

        panel.add(tabs,    BorderLayout.NORTH);
        panel.add(scroll,  BorderLayout.CENTER);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setBackground(BG_PANEL);
        footer.add(totals,  BorderLayout.NORTH);
        footer.add(actions, BorderLayout.CENTER);
        footer.add(nav,     BorderLayout.SOUTH);
        panel.add(footer, BorderLayout.SOUTH);

        return panel;
    }

    private JPanel buildTotalsPanel() {
        JPanel p = new JPanel(new GridLayout(3, 2, 0, 4));
        p.setBackground(BG_PANEL);
        p.setBorder(new CompoundBorder(
            new MatteBorder(1, 0, 1, 0, DIVIDER),
            new EmptyBorder(10, 16, 10, 16)));

        subtotalLabel = totalRow(p, "Subtotal");
        taxLabel      = totalRow(p, "Tax (9.5%)");
        totalLabel    = totalRow(p, "Total");
        totalLabel.setFont(new Font("SansSerif", Font.BOLD, 16));
        totalLabel.setForeground(ACCENT_BLUE);

        return p;
    }

    private JLabel totalRow(JPanel p, String label) {
        JLabel lbl = new JLabel(label);
        lbl.setForeground(label.equals("Total") ? TEXT_PRIMARY : TEXT_SECONDARY);
        lbl.setFont(new Font("SansSerif", label.equals("Total") ? Font.BOLD : Font.PLAIN, 13));
        JLabel val = new JLabel("$0.00", SwingConstants.RIGHT);
        val.setForeground(label.equals("Total") ? ACCENT_BLUE : TEXT_SECONDARY);
        val.setFont(new Font("SansSerif", label.equals("Total") ? Font.BOLD : Font.PLAIN, 13));
        p.add(lbl); p.add(val);
        return val;
    }

    private JPanel buildActionButtons() {
        JPanel p = new JPanel(new GridLayout(2, 2, 6, 6));
        p.setBackground(BG_PANEL);
        p.setBorder(new EmptyBorder(10, 12, 6, 12));

        JButton del   = roundBtn("🗑 Remove Item", new Color(80, 30, 30), ACCENT_RED);
        JButton clear = roundBtn("✖ Clear Order", new Color(70, 40, 20), ACCENT_ORANGE);
        JButton print = roundBtn("🖨 Print Receipt", BG_CARD, TEXT_PRIMARY);
        JButton pay   = roundBtn("💳 PAY", ACCENT_BLUE, Color.WHITE);
        pay.setFont(new Font("SansSerif", Font.BOLD, 14));

        del.addActionListener(e -> removeSelected());
        clear.addActionListener(e -> clearOrder());
        print.addActionListener(e -> printReceipt());
        pay.addActionListener(e -> processPayment());

        p.add(del); p.add(clear); p.add(print); p.add(pay);
        return p;
    }

    private JPanel buildBottomNav() {
        JPanel p = new JPanel(new GridLayout(1, 5, 0, 0));
        p.setBackground(BG_DARK);
        p.setBorder(new MatteBorder(1, 0, 0, 0, DIVIDER));
        p.setPreferredSize(new Dimension(0, 52));

        String[][] navItems = {{"1", "👤"}, {"Menu", "🍽"}, {"Orders", "📋"},
                               {"Transactions", "💳"}, {"Items", "📦"}};
        for (String[] item : navItems) {
            JPanel cell = new JPanel(new BorderLayout());
            cell.setOpaque(false);
            JLabel icon = new JLabel(item[1], SwingConstants.CENTER);
            icon.setFont(new Font("SansSerif", Font.PLAIN, 16));
            JLabel text = new JLabel(item[0], SwingConstants.CENTER);
            text.setFont(new Font("SansSerif", Font.PLAIN, 10));
            text.setForeground(TEXT_SECONDARY);
            cell.add(icon, BorderLayout.CENTER);
            cell.add(text, BorderLayout.SOUTH);
            cell.setBorder(new EmptyBorder(6, 0, 4, 0));
            cell.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            cell.addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { cell.setBackground(new Color(40, 40, 60)); cell.setOpaque(true); }
                public void mouseExited(MouseEvent e)  { cell.setOpaque(false); cell.repaint(); }
                public void mouseClicked(MouseEvent e) { navAction(item[0]); }
            });
            p.add(cell);
        }
        return p;
    }

    private JPanel buildStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(new Color(10, 10, 20));
        bar.setBorder(new EmptyBorder(3, 12, 3, 12));
        statusBar = new JLabel("Ready — Omega Gardens Hotel POS v1.0");
        statusBar.setForeground(TEXT_SECONDARY);
        statusBar.setFont(new Font("SansSerif", Font.PLAIN, 11));
        bar.add(statusBar, BorderLayout.WEST);
        JLabel ver = new JLabel("Java Swing Edition");
        ver.setForeground(new Color(80, 80, 110));
        ver.setFont(new Font("SansSerif", Font.PLAIN, 11));
        bar.add(ver, BorderLayout.EAST);
        return bar;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  ORDER LOGIC
    // ══════════════════════════════════════════════════════════════════════════
    private void addToOrder(MenuItem item) {
        for (OrderItem o : currentOrder) {
            if (o.menuItem.name.equals(item.name)) {
                o.qty++;
                refreshOrderTable();
                setStatus("+" + item.name + " (×" + o.qty + ")");
                return;
            }
        }
        currentOrder.add(new OrderItem(item, 1));
        refreshOrderTable();
        setStatus("Added: " + item.name + "  $" + String.format("%.2f", item.price));
    }

    private void removeSelected() {
        if (currentOrder.isEmpty()) return;
        // Remove last added or selected row in table
        int last = currentOrder.size() - 1;
        String name = currentOrder.get(last).menuItem.name;
        currentOrder.remove(last);
        refreshOrderTable();
        setStatus("Removed: " + name);
    }

    private void clearOrder() {
        if (currentOrder.isEmpty()) return;
        int res = JOptionPane.showConfirmDialog(this,
            "Clear the entire order for Table " + tableNumber + "?",
            "Clear Order", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
        if (res == JOptionPane.YES_OPTION) {
            currentOrder.clear();
            refreshOrderTable();
            setStatus("Order cleared.");
        }
    }

    private void refreshOrderTable() {
        orderTableModel.setRowCount(0);
        double sub = 0;
        for (OrderItem o : currentOrder) {
            double lineTotal = o.menuItem.price * o.qty;
            sub += lineTotal;
            orderTableModel.addRow(new Object[]{
                o.menuItem.emoji + " " + o.menuItem.name,
                o.qty,
                String.format("$%.2f", lineTotal)
            });
        }
        double tax   = sub * 0.095;
        double total = sub + tax;
        subtotalLabel.setText(String.format("$%.2f", sub));
        taxLabel.setText(String.format("$%.2f", tax));
        totalLabel.setText(String.format("$%.2f", total));
    }

    private void processPayment() {
        if (currentOrder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No items in the order!", "Empty Order", JOptionPane.WARNING_MESSAGE);
            return;
        }
        double sub   = getSubtotal();
        double tax   = sub * 0.095;
        double total = sub + tax;

        String[] methods = {"💵 Cash", "💳 Card", "📱 M-Pesa", "🏦 Bank Transfer"};
        String method = (String)JOptionPane.showInputDialog(this,
            String.format("Total: $%.2f\nSelect payment method:", total),
            "Process Payment", JOptionPane.PLAIN_MESSAGE, null, methods, methods[0]);

        if (method != null) {
            if (method.contains("Cash")) {
                String input = JOptionPane.showInputDialog(this,
                    String.format("Total: $%.2f\nCash received:", total), "Cash Payment", JOptionPane.PLAIN_MESSAGE);
                try {
                    double cash = Double.parseDouble(input);
                    if (cash < total) {
                        JOptionPane.showMessageDialog(this, "Insufficient cash!", "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    }
                    double change = cash - total;
                    JOptionPane.showMessageDialog(this,
                        String.format("✅ Payment received!\nChange due: $%.2f", change),
                        "Payment Complete", JOptionPane.INFORMATION_MESSAGE);
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount.", "Error", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } else {
                JOptionPane.showMessageDialog(this,
                    String.format("✅ %s payment of $%.2f processed!\nThank you!", method, total),
                    "Payment Complete", JOptionPane.INFORMATION_MESSAGE);
            }
            currentOrder.clear();
            orderCounter++;
            orderNumLabel.setText("Order #" + orderCounter);
            refreshOrderTable();
            setStatus("✅ Payment complete via " + method + "  |  Table " + tableNumber);
        }
    }

    private void printReceipt() {
        if (currentOrder.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Order is empty!", "No Items", JOptionPane.WARNING_MESSAGE);
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("══════════════════════════════\n");
        sb.append("     OMEGA GARDENS HOTEL\n");
        sb.append("      Restaurant Receipt\n");
        sb.append("══════════════════════════════\n");
        sb.append(String.format("Table: %-15s #%d%n", "T"+tableNumber, orderCounter));
        sb.append(String.format("Date: %s%n", new SimpleDateFormat("dd/MM/yyyy HH:mm").format(new Date())));
        sb.append("──────────────────────────────\n");
        double sub = 0;
        for (OrderItem o : currentOrder) {
            double line = o.menuItem.price * o.qty;
            sub += line;
            sb.append(String.format("%-18s x%d  $%.2f%n", o.menuItem.name, o.qty, line));
        }
        sb.append("──────────────────────────────\n");
        double tax = sub * 0.095, total = sub + tax;
        sb.append(String.format("%-22s $%.2f%n", "Subtotal", sub));
        sb.append(String.format("%-22s $%.2f%n", "Tax (9.5%)", tax));
        sb.append(String.format("%-22s $%.2f%n", "TOTAL", total));
        sb.append("══════════════════════════════\n");
        sb.append("    Thank you for dining with\n");
        sb.append("     Omega Gardens Hotel!\n");
        sb.append("══════════════════════════════\n");

        JTextArea area = new JTextArea(sb.toString());
        area.setFont(new Font("Monospaced", Font.PLAIN, 13));
        area.setEditable(false);
        area.setBackground(Color.WHITE);
        JScrollPane sp = new JScrollPane(area);
        sp.setPreferredSize(new Dimension(340, 400));
        JOptionPane.showMessageDialog(this, sp, "🖨 Receipt Preview", JOptionPane.PLAIN_MESSAGE);
    }

    private void newOrder() {
        if (!currentOrder.isEmpty()) {
            int r = JOptionPane.showConfirmDialog(this,
                "Start a new order? Current order will be cleared.", "New Order",
                JOptionPane.YES_NO_OPTION);
            if (r != JOptionPane.YES_OPTION) return;
        }
        currentOrder.clear();
        orderCounter++;
        orderNumLabel.setText("Order #" + orderCounter);
        refreshOrderTable();
        setStatus("New order started — Order #" + orderCounter);
    }

    private void changeTable(int delta) {
        tableNumber = Math.max(1, Math.min(30, tableNumber + delta));
        tableLabel.setText("T" + tableNumber);
        setStatus("Switched to Table " + tableNumber);
    }

    private void navAction(String label) {
        switch(label) {
            case "Menu" -> { loadCategory(currentCategory); setStatus("Menu view"); }
            case "Orders" -> showOrdersDialog();
            case "Transactions" -> showTransactionsDialog();
            default -> setStatus("'" + label + "' – feature available in full version");
        }
    }

    private void showOrdersDialog() {
        StringBuilder sb = new StringBuilder("Current Order – Table " + tableNumber + "\n\n");
        if (currentOrder.isEmpty()) sb.append("(empty)");
        else for (OrderItem o : currentOrder)
            sb.append(String.format("%-22s x%d  $%.2f%n", o.menuItem.name, o.qty, o.menuItem.price*o.qty));
        JOptionPane.showMessageDialog(this, new JTextArea(sb.toString()), "Orders", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showTransactionsDialog() {
        JOptionPane.showMessageDialog(this,
            "Transaction history will appear here.\n(Persistent storage can be added with SQLite/file I/O)",
            "Transactions", JOptionPane.INFORMATION_MESSAGE);
    }

    private double getSubtotal() {
        return currentOrder.stream().mapToDouble(o -> o.menuItem.price * o.qty).sum();
    }

    private void setStatus(String msg) {
        statusBar.setText(msg);
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  STYLE HELPERS
    // ══════════════════════════════════════════════════════════════════════════
    private void styleOrderTable(JTable t) {
        t.setBackground(BG_PANEL);
        t.setForeground(TEXT_PRIMARY);
        t.setFont(new Font("SansSerif", Font.PLAIN, 12));
        t.setRowHeight(32);
        t.setShowGrid(false);
        t.setIntercellSpacing(new Dimension(0, 1));
        t.setSelectionBackground(new Color(66, 135, 245, 80));
        t.setSelectionForeground(TEXT_PRIMARY);
        t.getTableHeader().setBackground(BG_DARK);
        t.getTableHeader().setForeground(TEXT_SECONDARY);
        t.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        t.getTableHeader().setBorder(new MatteBorder(0, 0, 1, 0, DIVIDER));
        t.getColumnModel().getColumn(0).setPreferredWidth(190);
        t.getColumnModel().getColumn(1).setPreferredWidth(40);
        t.getColumnModel().getColumn(2).setPreferredWidth(70);

        // Alternating rows
        t.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
            public Component getTableCellRendererComponent(JTable table, Object value,
                    boolean isSelected, boolean hasFocus, int row, int col) {
                Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, col);
                if (!isSelected) c.setBackground(row % 2 == 0 ? BG_PANEL : BG_CARD);
                c.setForeground(col == 2 ? ACCENT_BLUE : TEXT_PRIMARY);
                ((JLabel)c).setBorder(new EmptyBorder(0, 8, 0, 8));
                if (col == 2) ((JLabel)c).setHorizontalAlignment(SwingConstants.RIGHT);
                return c;
            }
        });
    }

    private void styleTextField(JTextField f, String placeholder) {
        f.setBackground(BG_CARD);
        f.setForeground(TEXT_SECONDARY);
        f.setCaretColor(TEXT_PRIMARY);
        f.setBorder(new CompoundBorder(
            new LineBorder(DIVIDER, 1, true),
            new EmptyBorder(6, 10, 6, 10)));
        f.setFont(new Font("SansSerif", Font.PLAIN, 13));
        f.setText(placeholder);
        f.addFocusListener(new FocusAdapter() {
            public void focusGained(FocusEvent e) {
                if (f.getText().equals(placeholder)) { f.setText(""); f.setForeground(TEXT_PRIMARY); }
            }
            public void focusLost(FocusEvent e) {
                if (f.getText().isBlank()) { f.setText(placeholder); f.setForeground(TEXT_SECONDARY); }
            }
        });
    }

    private JButton roundBtn(String text, Color bg, Color fg) {
        JButton b = new JButton(text) {
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D)g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(getBackground());
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
                super.paintComponent(g);
            }
        };
        b.setFont(new Font("SansSerif", Font.BOLD, 12));
        b.setForeground(fg);
        b.setBackground(bg);
        b.setBorder(new EmptyBorder(9, 16, 9, 16));
        b.setFocusPainted(false);
        b.setContentAreaFilled(false);
        b.setOpaque(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        b.addMouseListener(new MouseAdapter() {
            Color orig = bg;
            public void mouseEntered(MouseEvent e) { b.setBackground(bg.brighter()); }
            public void mouseExited(MouseEvent e)  { b.setBackground(orig); }
        });
        return b;
    }

    private JButton iconBtn(String text) {
        JButton b = new JButton(text);
        b.setFont(new Font("SansSerif", Font.PLAIN, 12));
        b.setForeground(TEXT_SECONDARY);
        b.setBackground(BG_CARD);
        b.setBorder(new EmptyBorder(4, 8, 4, 8));
        b.setFocusPainted(false);
        b.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return b;
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  INNER CLASSES
    // ══════════════════════════════════════════════════════════════════════════
    static class MenuItem {
        String name, emoji;
        double price;
        MenuItem(String name, double price, String emoji) {
            this.name = name; this.price = price; this.emoji = emoji;
        }
    }

    static class OrderItem {
        MenuItem menuItem;
        int qty;
        OrderItem(MenuItem m, int q) { menuItem = m; qty = q; }
    }

    // ══════════════════════════════════════════════════════════════════════════
    //  MAIN
    // ══════════════════════════════════════════════════════════════════════════
    public static void main(String[] args) {
        // Use system look and feel as base, then override
        try { UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName()); }
        catch (Exception ignored) {}

        UIManager.put("OptionPane.background",        new Color(28, 28, 45));
        UIManager.put("Panel.background",             new Color(28, 28, 45));
        UIManager.put("OptionPane.messageForeground", new Color(245, 245, 250));
        UIManager.put("Button.background",            new Color(66, 135, 245));
        UIManager.put("Button.foreground",            Color.WHITE);
        UIManager.put("Button.focus",                 new Color(0,0,0,0));
        UIManager.put("TextField.background",         new Color(38, 38, 58));
        UIManager.put("TextField.foreground",         new Color(245, 245, 250));
        UIManager.put("ScrollBar.thumb",              new Color(55, 55, 80));
        UIManager.put("ScrollBar.track",              new Color(28, 28, 45));

        SwingUtilities.invokeLater(RestaurantPOS::new);
    }
}