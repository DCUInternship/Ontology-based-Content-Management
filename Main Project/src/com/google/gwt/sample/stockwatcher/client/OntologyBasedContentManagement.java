package com.google.gwt.sample.stockwatcher.client;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.IFrameElement;
import com.google.gwt.dom.client.Style.Position;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.*;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.event.dom.client.LoadEvent;
import com.google.gwt.event.dom.client.LoadHandler;
import com.google.gwt.event.dom.client.MouseMoveEvent;
import com.google.gwt.event.dom.client.MouseMoveHandler;
import com.google.gwt.event.logical.shared.OpenEvent;
import com.google.gwt.event.logical.shared.OpenHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;
import com.google.gwt.user.client.ui.FormPanel.SubmitCompleteEvent;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class OntologyBasedContentManagement implements EntryPoint {
	private RootPanel rootPanel = RootPanel.get("stockList");
	private RootPanel root2Panel = RootPanel.get("newList");
	private Anchor new_page = new Anchor("Ontology Tree");
	private Anchor home_page = new Anchor("Home");
	private Anchor download_repository = new Anchor("Download Repository");
	private String export_fp = "";
	private boolean repository_downloaded = false;
	/*
	 * Create interface instances
	 */
	private DialogBox dBox = new DialogBox(false);
	private PopupPanel popup = new PopupPanel();
	private HTML message;
	private CheckBox cb;
	private Tree browseTree = new Tree();

	private String baseURI;
	private String url = "http://www.cngl.ie";
	private String ontName;
	private String netOntName;
	private ScrollPanel grid_scroll = new ScrollPanel(); // page 2, instances

	private VerticalPanel mainPanel = new VerticalPanel(); // Holds the table
	private VerticalPanel secondPanel = new VerticalPanel();
	private VerticalPanel radioButtonPanel = new VerticalPanel();
	private VerticalPanel dialogBoxContents = new VerticalPanel();
	private VerticalPanel popupContents = new VerticalPanel();
	private VerticalPanel instance_link = new VerticalPanel();
	private VerticalPanel queryPanel = new VerticalPanel(); // holds panel for
															// querying
	private VerticalPanel bottomOfScreen = new VerticalPanel(); // Subject
																// search &
																// triple table
	private VerticalPanel page2Panel = new VerticalPanel();

	private FlexTable Ont_Table = new FlexTable(); // Contains 3 ListBoxes for
													// Ontology
	private FlexTable tripleTable = new FlexTable(); // table for hold triples
														// to be sent
	private FlexTable ft = new FlexTable();
	private FlexTable instance_grid = new FlexTable();

	private HorizontalPanel addPanel = new HorizontalPanel();
	private HorizontalPanel searchPanel = new HorizontalPanel(); // find word in
																	// content
	private HorizontalPanel loadOntologyInternet = new HorizontalPanel();
	private HorizontalPanel suggested_checkbox = new HorizontalPanel();
	private HorizontalPanel tree_grid = new HorizontalPanel();
	private HorizontalPanel loading_and_ontologybox = new HorizontalPanel();

	private SimplePanel dialogBoxholder = new SimplePanel();
	private SimplePanel popupHolder = new SimplePanel();
	private TextBox entercontext = new TextBox();
	private TextBox content = new TextBox(); // search webpage for content
	private TextBox webBox = new TextBox();
	private TextBox ontology_from_disk = new TextBox();
	private TextBox subjectQuery = new TextBox();
	private TextBox contextQuery = new TextBox();
	private TextBox ontology_from_internet = new TextBox(); // enter URL of
															// ontology
	private TextBox user_enter_subject = new TextBox(); // user enter word not

	private Button search = new Button(); // button to trigger search
	private Button webSend = new Button();
	private Button save = new Button(); // saves selected triples to arraylist
	private Button load_ontology_web_button = new Button();
	private Button user_subject_button = new Button();
	private Button close = new Button();
	private Button closePopup = new Button("Close");
	private Button loadFile = new Button("Load File");
	private Button queryButton = new Button("Send Query");

	private RadioButton radioA = new RadioButton("group", "Subject");
	private RadioButton radioB = new RadioButton("group", "Object");

	private Label suggestion_label = new Label("Suggestion");
	private Label context_label = new Label("Enter Context");
	private Label ontology_label = new Label("Ontologies");
	private Label classes_label = new Label("Classes");
	private Label object_prop_label = new Label("Object Properties");
	private Label data_prop_label = new Label("Data Properties");
	private Label subject_label = new Label("Subject");
	private Label lblObjectProperties = new Label("Object Properties");
	private Label lblDataProperties = new Label("Data Properties");

	private ListBox ontology_Classes, property_Resources, property_Literals, ontologies;
	private Frame frame; // uses HTTPRequest to get website
	private String frameWidth, frameHeight;
	private String subject = "";
	private String path_of_uploaded_file = "";
	private int selectedListIndex = -1;
	private int row = 1; // keeps track of rows in flex table
	private ArrayList<String> list = new ArrayList<String>(); // To be sent to
																// server
	private ArrayList<String> classes = new ArrayList<String>();
	private ArrayList<String> properties = new ArrayList<String>();
	private ArrayList<String> literals = new ArrayList<String>();
	private List<String[]> sugT = new ArrayList<String[]>();
	private ArrayList<String[]> insert_ToTable = new ArrayList<String[]>(); // suggested
																			// triples

	private final GreetingServiceAsync greetingService = GWT.create(GreetingService.class);
	private static Logger logger = Logger.getLogger("");
	/*
	 * file instances
	 */
	private String filename = "";
	private static final String LOADING_ITEMS = "Loading items. . .";
	private FileUpload fileUpload = new FileUpload();
	private FormPanel form = new FormPanel();
	private Label statusLabel = new Label();
	private static String filepathofexport;
	private ArrayList<Ontology> ontology = new ArrayList<Ontology>();
	private int left = (int) (Window.getClientWidth() / 1.5), top = (int) Window.getClientHeight() / 4;
	private String link_to_content_page;
	private int rowIndex = 0;
	private final Label lblOntologies = new Label("Ontologies");
	private final Label lblClasses = new Label("Classes");
	private final TextBox ont_netName = new TextBox();

	/*
	 * Entry Point method.
	 */
	@SuppressWarnings("deprecation")
	@Override
	public void onModuleLoad() {
		radioA.setValue(true);
		/*
		 * Create file interface
		 */
		// Create a FormPanel and point it at a service.
		form = new FormPanel();
		form.setAction(GWT.getModuleBaseURL() + "greet");
		form.setEncoding(FormPanel.ENCODING_MULTIPART);
		form.setMethod(FormPanel.METHOD_POST);
		form.setWidget(fileUpload);
		fileUpload.setSize("100%", "100%");
		fileUpload.setName("Ontology");
		ClickHandler load_handler = new ClickHandler() {
			@Override
			public void onClick(final ClickEvent event) {
				ontName = fileUpload.getFilename().substring(fileUpload.getFilename().lastIndexOf('\\') + 1);
				form.submit();
			}
		};
		// secondPanel.add(uploadedOntologies);

		/*
		 * end file creation
		 */
		logger.log(Level.SEVERE, "Log!");
		double wi = Window.getClientWidth() / 3.5;
		String tablewidth = Double.toString(wi);
		tripleTable.getColumnFormatter().setWidth(0, tablewidth);
		tripleTable.getColumnFormatter().setWidth(1, tablewidth);
		tripleTable.getColumnFormatter().setWidth(2, tablewidth);
		// tripleTable.setStyleName("Prompt-User");
		tripleTable.setText(0, 0, "Subject");
		tripleTable.setText(0, 1, "Predicate");
		tripleTable.setText(0, 2, "Object");
		// tripleTable.getRowFormatter().addStyleName(0, "Prompt-User");
		tripleTable.getColumnFormatter().addStyleName(0, "columnOne");
		tripleTable.getColumnFormatter().addStyleName(1, "columnTwo");
		tripleTable.getColumnFormatter().addStyleName(2, "columnThree");
		tripleTable.getColumnFormatter().addStyleName(3, "remainingColumns");
		tripleTable.getColumnFormatter().addStyleName(4, "remainingColumns");
		tripleTable.getColumnFormatter().addStyleName(5, "remainingColumns");
		tripleTable.addStyleName("tripleTable");
		row = tripleTable.getRowCount();
		frameWidth = String.valueOf(Window.getClientWidth() / 3.3) + "px";
		ontology_from_disk.setText("/Users/markhender/ontologies/pizzas/pizza.rdf");
		ontology_from_disk.setWidth("340px");
		frame = new Frame();
		frame.setUrl(url);
		frameWidth = String.valueOf(Window.getClientWidth() - 20) + "px"; // works
																			// cross
																			// browsers
		frameHeight = String.valueOf(String.valueOf(Window.getClientHeight() / 1.3) + "px");
		frame.setSize(frameWidth, "487px");
		frame.setVisible(true);

		/*
		 * Popup Panel
		 */
		popupHolder.add(closePopup);

		popup.setWidget(popupContents);
		closePopup.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				int end_of_list = ft.getRowCount();
				int count = 1;

				while (count < end_of_list) {
					logger.log(Level.SEVERE, "line");
					CheckBox box = (CheckBox) ft.getWidget(count, 3);
					if (box.getValue()) {
						// tripleTable.setText(tripleTable.getRowCount(), 0,
						// ft.getText(count, 0));
						printSuggestedSubject(ft.getText(count, 0));
						addPredicate(ft.getText(count, 1));
						// if (ft.getText(count, 1).endsWith("*"))
						// addLitObject(ft.getText(count, 2));
						// else
						addSuggestedObject(ft.getText(count, 2));
						logger.log(Level.SEVERE, "" + ft.getText(count, 0) + "," + ft.getText(count, 0) + "," + ft.getText(count, 2));
					}
					count++;
				}
				logger.log(Level.SEVERE, "BINGO");
				ft.removeAllRows();

				popup.hide();
				popupContents.clear();
				popupContents.add(popupHolder);
			}
		});
		content.setText("Search for content in webpage");
		content.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				content.setFocus(true);
				if (content.getText().equals("Search for content in webpage"))
					content.setText("");
				else
					content.selectAll();
			}
		});
		bottomOfScreen.setCellVerticalAlignment(tripleTable, HasVerticalAlignment.ALIGN_MIDDLE);
		bottomOfScreen.setCellHorizontalAlignment(tripleTable, HasHorizontalAlignment.ALIGN_CENTER);
		searchPanel.add(content); // content search box
		searchPanel.add(search); // trigger content search button
		search.setHeight("37px");
		dBox.setText("Triple Report");
		close.setText("Close");
		close.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				dialogBoxContents.clear();
				dBox.hide();

			}
		});
		dialogBoxholder.add(close);
		radioButtonPanel.add(radioA);
		radioButtonPanel.add(radioB);
		searchPanel.add(radioButtonPanel);
		bottomOfScreen.add(searchPanel);
		bottomOfScreen.add(tripleTable);
		tripleTable.setSize("1247px", "67px");
		// bottomOfScreen.setSpacing(10);
		search.setText("Enter");
		content.setSize("282px", "29px");
		ListBox listbox = new ListBox();

		listbox.setWidth("100px");
		listbox.setHeight("400px");
		save.setText("Save");
		listbox.setVisible(false);
		/*
		 * before new page
		 */
		final PopupPanel contextpanel = new PopupPanel();

		final Button ok = new Button("OK");
		final HorizontalPanel hori = new HorizontalPanel();
		contextpanel.setWidget(hori);
		entercontext.setText("context");
		hori.add(ok);
		final ClickHandler download_handler = new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				greetingService.downloadRepository(entercontext.getText(), new downloadRepository());
				if (repository_downloaded)
					loadPageTwo(export_fp);
				repository_downloaded = true;
				logger.log(Level.SEVERE, "download_handler called");
			}
		};
		ClickHandler newpage_handler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				contextpanel.center();
				ok.addClickHandler(new ClickHandler() {
					@Override
					public void onClick(ClickEvent event) {
						if (repository_downloaded)
							loadPageTwo(export_fp);
						else {
							download_handler.onClick(event);

						}
						contextpanel.hide();

					}

				});
				logger.log(Level.SEVERE, "export path: " + filepathofexport);

			}

		};
		/* Return to homepage */
		home_page.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				loadHomePage();
			}
		});

		mainPanel.setStyleName("mainPanel");
		mainPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		bottomOfScreen.setSize("1250px", "164px");
		addPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		rootPanel.add(addPanel, 0, 10);
		rootPanel.add(frame, 0, addPanel.getAbsoluteTop() + addPanel.getOffsetHeight() + 20);
		rootPanel.add(form, 295, frame.getAbsoluteTop() + frame.getOffsetHeight() + 20);
		rootPanel.add(mainPanel, 744, frame.getAbsoluteTop() + frame.getOffsetHeight() + 20);
		rootPanel.add(bottomOfScreen, 0, mainPanel.getAbsoluteTop() + mainPanel.getOffsetHeight() + 100);
		rootPanel.add(secondPanel, 296, form.getAbsoluteTop() + form.getOffsetHeight());
		VerticalPanel verticalPanel = new VerticalPanel();
		verticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);
		bottomOfScreen.add(verticalPanel);
		verticalPanel.setSize("1246px", "53px");
		verticalPanel.add(new_page);
		verticalPanel.add(download_repository);
		download_repository.addClickHandler(download_handler);
		new_page.addClickHandler(newpage_handler);

		/*
		 * Page 2 handlers and such
		 */
		subjectQuery.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				subjectQuery.setText("");
			}
		});
		subjectQuery.addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				Label test_label = new Label();
				test_label.setText("Enter subject value or leave blank");
				test_label.getElement().getStyle().setPosition(Position.ABSOLUTE);
			}

		});

		frame.addLoadHandler(new LoadHandler() {

			@Override
			public void onLoad(LoadEvent event) {
				IFrameElement iframe = IFrameElement.as(frame.getElement());
				logger.log(Level.SEVERE, "Page: " + urlChange(frame));

			}

		});

		final AsyncCallback<ArrayList<String>> ontology_class = new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				classes = result;
				logger.log(Level.SEVERE, "size; " + result.size());
				ontology.get(ontology.size() - 1).setClasses(result);
				if (ontology.size() == 1)
					populate_ClassBox(0);
			}
		};
		final AsyncCallback<ArrayList<String>> property_resource = new AsyncCallback<ArrayList<String>>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				properties = result;
				ontology.get(ontology.size() - 1).setProperties(result);
				if (ontology.size() == 1)
					populate_PropertyBox(0);
			}
		};
		final AsyncCallback<ArrayList<String>> property_literal = new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				literals = result;
				ontology.get(ontology.size() - 1).setLiterals(result);
				if (ontology.size() == 1)
					populate_LiteralBox(0);
			}
		};
		final AsyncCallback<Integer[]> subjectIndexOfContent = new AsyncCallback<Integer[]>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(Integer[] result) {
				if (result[0] != -99 && result[1] != -99) {
					// word found
					printSubject();
				} else
					Window.alert("Word not found");
			}

		};

		final AsyncCallback<String> getOntName = new AsyncCallback<String>() {
			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(String result) {
				ontName = result;
				ontology.get(ontology.size() - 1).setName(result);

				logger.log(Level.SEVERE, ("OntologyName = " + ontName));
			}

		};

		final AsyncCallback<String> geturi = new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(String result) {
				baseURI = result;
				ontology.get(ontology.size() - 1).setBaseURI(result);
				logger.log(Level.SEVERE, "The baseURI is " + baseURI);
			}

		};
		final AsyncCallback<ArrayList<String[]>> suggestedTriples = new AsyncCallback<ArrayList<String[]>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(ArrayList<String[]> result) {
				logger.log(Level.SEVERE, "First");
				populateSuggestedTriples(result);
				Window.alert("Pass");
			}

		};
		suggestion_label.setText("Help");
		suggestion_label.getElement().getStyle().setPosition(Position.ABSOLUTE);
		suggestion_label.addMouseMoveHandler(new MouseMoveHandler() {

			@Override
			public void onMouseMove(MouseMoveEvent event) {
				suggestion_label.getElement().getStyle().setTop(event.getClientX(), Unit.PX);
				suggestion_label.getElement().getStyle().setLeft(event.getClientY(), Unit.PX);
			}

		});
		tripleTable.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				com.google.gwt.user.client.ui.HTMLTable.Cell cell = tripleTable.getCellForEvent(event);
				int cellIndex = cell.getCellIndex();
				int rowIndex = cell.getRowIndex();
				if (cellIndex == 4 || cellIndex == 0) {
					logger.log(Level.SEVERE, "Sending: " + tripleTable.getText(rowIndex, 0));
					if (cellIndex == 0) {

					}
					greetingService.suggestedTriples(tripleTable.getHTML(rowIndex, 0), suggestedTriples);
				} else if (cellIndex == 3) {
					tripleTable.removeRow(rowIndex);
				}
			}
		});

		search.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String blah = content.getText();
				if (radioA.isChecked() || !radioB.isChecked())
					greetingService.wordExists(blah, webBox.getText(), subjectIndexOfContent);
				else {
					printSubject();
				}
				content.setFocus(true);
				content.selectAll();
			}
		});

		content.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {

				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					if (radioA.isChecked() || !radioB.isChecked())
						greetingService.wordExists(content.getText(), webBox.getText(), subjectIndexOfContent);
					else
						printSubject();
					content.setFocus(true);
					content.selectAll();
				}
			}
		});
		save.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				String[] temp = new String[3];
				temp = getTriples();
				repository_downloaded = false;
			}
		});

		secondPanel.setSize("402px", "60px");
		secondPanel.add(loadFile);
		loadFile.setHeight("27px");
		loadFile.addClickHandler(load_handler);
		secondPanel.add(loadOntologyInternet);
		ontology_from_internet.setText("Enter URL of Ontology");

		loadOntologyInternet.add(ontology_from_internet);
		ontology_from_internet.setWidth("215px");
		ont_netName.setText("Enter Name");
		ont_netName.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				ont_netName.setText("");
			}
		});
		ontology_from_internet.addFocusHandler(new FocusHandler() {
			@Override
			public void onFocus(FocusEvent event) {
				ontology_from_internet.setText("");
			}
		});

		loadOntologyInternet.add(ont_netName);
		ont_netName.setWidth("149px");
		load_ontology_web_button.setHTML("Go");
		loadOntologyInternet.add(load_ontology_web_button);
		load_ontology_web_button.setSize("32px", "32px");
		mainPanel.add(Ont_Table);
		Ont_Table.setSize("506px", "94px");
		Ont_Table.setText(3, 0, "Class"); // 3 columns
		Ont_Table.setText(3, 1, "Object Property");
		Ont_Table.setText(3, 2, "Data Property");
		ontology_Classes = new ListBox(); // Ontology class listbox
		property_Resources = new ListBox(); // " property listbox
		property_Literals = new ListBox(); // " individual listbox
		ontology_Classes.setSize("100%", "100%");
		property_Resources.setSize("100%", "100%");
		property_Literals.setSize("100%", "100%");
		mainPanel.setCellHeight(Ont_Table, "18px");
		mainPanel.setCellWidth(Ont_Table, "152px");
		lblOntologies.setStyleName("Label");

		Ont_Table.setWidget(0, 1, lblOntologies);
		lblOntologies.setSize("100%", "100%");

		ontologies = new ListBox();
		Ont_Table.setWidget(1, 1, ontologies);
		ontologies.setSize("100%", "100%");
		ontologies.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int listIndex = ontologies.getSelectedIndex();

				populate_ClassBox(listIndex);
				populate_PropertyBox(listIndex);
				populate_LiteralBox(listIndex);
				logger.log(Level.SEVERE, "web uri is: " + ontology.get(ontologies.getSelectedIndex()).getBaseURI());
			}
		});
		lblClasses.setStyleName("Label");

		Ont_Table.setWidget(2, 0, lblClasses);
		Ont_Table.getCellFormatter().setWidth(2, 0, "152px");
		lblClasses.setSize("100%", "100%");
		lblObjectProperties.setStyleName("Label");

		Ont_Table.setWidget(2, 1, lblObjectProperties);
		Ont_Table.getCellFormatter().setWidth(2, 1, "152px");
		lblObjectProperties.setSize("100%", "100%");
		lblDataProperties.setStyleName("Label");
		Ont_Table.setWidget(2, 2, lblDataProperties);
		Ont_Table.getCellFormatter().setWidth(2, 2, "152px");
		lblDataProperties.setSize("100%", "100%");

		Ont_Table.setWidget(3, 0, ontology_Classes);
		Ont_Table.setWidget(3, 1, property_Resources);
		Ont_Table.setWidget(3, 2, property_Literals);
		Ont_Table.getCellFormatter().setWidth(3, 2, "152px\n");
		ontology_Classes.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				int listIndex = ontology_Classes.getSelectedIndex();
				String uri = ontology.get(ontologies.getSelectedIndex()).getBaseURI();
				String item = uri + ontology_Classes.getItemText(listIndex);
				addObject(item);
			}

		});
		property_Resources.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				// TODO Auto-generated method stub
				int listIndex = property_Resources.getSelectedIndex();
				logger.log(Level.SEVERE, property_Resources.getItemText(listIndex));
				if (!(property_Resources.getItemText(listIndex).equals("RDF.type"))) {
					logger.log(Level.SEVERE, "not rdf.type");
					String uri = ontology.get(ontologies.getSelectedIndex()).getBaseURI();
					String item = uri + property_Resources.getItemText(listIndex);
					addPredicate(item);
				} else {
					logger.log(Level.SEVERE, "rdf.type");
					String item = property_Resources.getItemText(listIndex);
					addPredicate(item);
				}

			}

		});

		property_Literals.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				if (property_Literals.getItemCount() == 0)
					Window.alert("This list is empty!");
				else {
					int listIndex = property_Literals.getSelectedIndex();
					String uri = ontology.get(ontologies.getSelectedIndex()).getBaseURI();
					String item = uri + property_Literals.getItemText(listIndex);
					addPredicate(item);
				}
			}
		});
		addPanel.setSize("380px", "32px");
		addPanel.add(webBox);

		webBox.setText(url);
		webBox.setWidth("340px");

		// listen for keyboard events in the textbox
		webBox.addKeyUpHandler(new KeyUpHandler() {
			@Override
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					frame.setUrl(webBox.getText());
					content.setFocus(true);
					content.selectAll();
				}
			}
		});
		addPanel.add(webSend);
		webSend.setHeight("32px");

		webSend.setText("GO");

		// Listen for mouse events on webSend Button
		// to get new website
		webSend.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				frame.setUrl(webBox.getText()); // take url from textbox
				logger.log(Level.SEVERE, frame.getUrl());
				content.setFocus(true);
				content.selectAll();
			}
		});

		/*
		 * Adding Change listener to listboxes
		 */

		/*
		 * File submit handling
		 */
		final AsyncCallback<String> pathfile = new AsyncCallback<String>() {

			@Override
			public void onFailure(Throwable caught) {
				Window.alert("Fail");
			}

			@Override
			public void onSuccess(String result) {
				path_of_uploaded_file = result;
				ontology.get(ontology.size() - 1).setFilePath(result);
				Window.alert("Pass");
				greetingService.greetServer(list, path_of_uploaded_file, 1, ontology_class);
				greetingService.greetServer(list, path_of_uploaded_file, 2, property_resource);
				greetingService.greetServer(list, path_of_uploaded_file, 3, property_literal);

				ontologies.addItem(ontology.get(ontology.size() - 1).getName());

				// logger.log(Level.SEVERE, baseURI);
			}

		};
		form.addSubmitCompleteHandler(new FormPanel.SubmitCompleteHandler() {

			@Override
			public void onSubmitComplete(SubmitCompleteEvent event) {
				logger.log(Level.SEVERE, "form title: " + form.getTitle());
				form.reset();
				greetingService.filePath(pathfile);
				greetingService.getBaseURI(geturi);
				ontology.add(new Ontology(ontName, path_of_uploaded_file, baseURI, classes, properties, literals));
				if (ontology.size() == 1) {
					// populate_listBoxes();
				}
				logger.log(Level.SEVERE, "baseuri = " + baseURI);
			}

		});
		final AsyncCallback<ArrayList<String>> ontologyComponents = new AsyncCallback<ArrayList<String>>() {

			@Override
			public void onFailure(Throwable caught) {

			}

			@Override
			public void onSuccess(ArrayList<String> result) {
				if (result.contains("ThisIsClassSection")) {
					logger.log(Level.SEVERE, "class size; " + result.size());
					result.remove(result.indexOf("ThisIsClassSection"));
					ontology.get(ontology.size() - 1).setClasses(result);
					if (ontology.size() == 1)
						populate_ClassBox(0);
					greetingService.getBaseURI(geturi);
				}

				else if (result.contains("ThisIsObjectProperties")) {
					logger.log(Level.SEVERE, "prop size; " + result.size());
					int index = result.indexOf("ThisIsObjectProperties");
					result.remove(index);
					ontology.get(ontology.size() - 1).setProperties(result);
					if (ontology.size() == 1)
						populate_PropertyBox(0);
				} else {
					logger.log(Level.SEVERE, "liter size; " + result.size());
					result.remove(result.indexOf("ThisIsDataProperties"));
					ontology.get(ontology.size() - 1).setLiterals(result);
					if (ontology.size() == 1)
						populate_LiteralBox(0);
				}
			}

		};
		load_ontology_web_button.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				netOntName = ont_netName.getText();
				ontology.add(new Ontology(ont_netName.getText(), ontology_from_internet.getText(), baseURI, classes, properties, literals));
				greetingService.ontologyComponents(list, ontology_from_internet.getText(), 1, ontologyComponents);
				greetingService.ontologyComponents(list, ontology_from_internet.getText(), 2, ontologyComponents);
				greetingService.ontologyComponents(list, ontology_from_internet.getText(), 3, ontologyComponents);

				ontologies.addItem(ontology.get(ontology.size() - 1).getName());
				// logger.log(Level.SEVERE, "web uri is: " +
				// ontology.get(ontologies.getSelectedIndex()).getBaseURI());
			}
		});
		/*
		 * 
		 * page 2
		 */
		tree_grid.add(browseTree);

		instance_link.add(instance_grid);
		instance_link.addStyleName("treeAndGrid");
		instance_grid.addStyleName("treeAndGrid");
		// instance_grid.addClickHandler(new ClickHandler())

		tree_grid.add(instance_link);
		tree_grid.add(queryPanel);
		page2Panel.add(context_label);
		page2Panel.add(entercontext);
		page2Panel.add(tree_grid);
		page2Panel.addStyleName("treeAndGrid");
		instance_grid.setHeight(page2Panel.getOffsetHeight() + "px");
		root2Panel.setVisible(false);

		final ClickHandler getWebsite = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				com.google.gwt.user.client.ui.HTMLTable.Cell cell = instance_grid.getCellForEvent(event);

				instance_grid.getRowFormatter().removeStyleName(rowIndex, "selectCell");
				int cellIndex = cell.getCellIndex();
				rowIndex = cell.getRowIndex();
				instance_grid.removeStyleName("selectCell");
				if (cellIndex == 0) {
					instance_grid.getRowFormatter().addStyleName(rowIndex, "selectCell");
					// instance_grid.getColumnFormatter().addStyleName(cellIndex,
					// "selectCell");
					link_to_content_page = instance_grid.getText(rowIndex, 0);
					link_to_content_page = link_to_content_page.substring(0, link_to_content_page.lastIndexOf('/'));
					logger.log(Level.SEVERE, "URL: " + link_to_content_page);
				}

			}
		};
		final ClickHandler page2_queryHandler = new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				if (subjectQuery.getText().equals("")) {

				}
				instance_grid.removeAllRows();
				greetingService.getQueryInstances(
						subjectQuery.getText().equals("") ? "NONE" : webBox.getText().concat("/" + subjectQuery.getText().replace(' ', '_')),
						property_Resources.getItemText(property_Resources.getSelectedIndex()).equals("NONE") ? "NONE" : ontology
								.get(ontologies.getSelectedIndex()).getBaseURI()
								.concat(property_Resources.getItemText(property_Resources.getSelectedIndex())),

						ontology_Classes.getItemText(ontology_Classes.getSelectedIndex()).equals("NONE") ? "NONE" : ontology
								.get(ontologies.getSelectedIndex()).getBaseURI()
								.concat(ontology_Classes.getItemText(ontology_Classes.getSelectedIndex())),
						entercontext.getText().equals("") ? "NONE" : entercontext.getText(), new queryInstances());
			}

		};
	//	instance_grid.addClickHandler(getWebsite);
		browseTree.addStyleName("treeAndGrid");
		queryButton.addClickHandler(page2_queryHandler);
	}

	protected void addSuggestedObject(String item) {
		tripleTable.setText(row, 2, item);
	}

	protected void printSuggestedSubject(String content) {

		subject = webBox.getText().concat("/" + content.replace(' ', '_'));
		row = tripleTable.getRowCount();
		// logger.log(Level.SEVERE, "rowcount:" + row);
		content.replace(' ', '_');
		if ((tripleTable.getText(row - 1, 2).isEmpty())) {
			row--;
		}

		if (radioA.isChecked() || !radioB.isChecked()) {
			tripleTable.setText(row, 0, subject);
		} else {
			tripleTable.setText(row, 2, subject);
		}

		tripleTable.setWidget(0, 5, save);
		Button removeButton = new Button("x"); // Will remove a triple from the
												// list
		Button suggestion = new Button("Suggestions");
		tripleTable.setWidget(row, 3, removeButton);

		tripleTable.setWidget(row, 4, suggestion);
		removeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				tripleTable.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent click) {
						com.google.gwt.user.client.ui.HTMLTable.Cell cell = tripleTable.getCellForEvent(event);
						int cellIndex = cell.getCellIndex();
						logger.log(Level.SEVERE, "cell:" + cellIndex);
						// if (cellIndex == 3) {
						// tripleTable.removeRow(rowIndex);
						// }
						rowIndex = tripleTable.getRowCount();
					}

				});
			}

		});

	}

	/*
	 * Open up new web page from instance
	 */
	protected final ClickHandler link_to_page = new ClickHandler() {

		@Override
		public void onClick(ClickEvent event) {
			logger.log(Level.SEVERE, "URL: " + link_to_content_page);
			com.google.gwt.user.client.ui.HTMLTable.Cell cell = instance_grid.getCellForEvent(event);
			instance_grid.getRowFormatter().removeStyleName(rowIndex, "selectCell");
			int cellIndex = cell.getCellIndex();
			rowIndex = cell.getRowIndex();
			instance_grid.removeStyleName("selectCell");
			if (cellIndex == 0) {
				instance_grid.getRowFormatter().addStyleName(rowIndex, "selectCell");
				link_to_content_page = instance_grid.getText(rowIndex, 0);
				link_to_content_page = link_to_content_page.substring(0, link_to_content_page.lastIndexOf('/'));
				logger.log(Level.SEVERE, "URL: " + link_to_content_page);
			}
			Window.open(link_to_content_page, "Content Page", "menubar=yes,location=yes,resizable=yes,scrollbars=yes,status=yes");
		}

	};

	protected void populate_ClassBox(int index) {
		ontology_Classes.clear();

		ontology_Classes.addItem("NONE");
		// menuBar_3.addItem(text, cmd)
		// String variable = "";
		Iterator<String> it = ontology.get(index).getClasses().iterator();
		while (it.hasNext()) {
			final String variable = it.next();
			ontology_Classes.addItem(variable);
		}

	}

	protected void populate_PropertyBox(int index) {
		property_Resources.clear();
		Iterator<String> it = ontology.get(index).getProperties().iterator();
		property_Resources.addItem("NONE");
		property_Resources.addItem("RDF.type");
		while (it.hasNext()) {
			property_Resources.addItem(it.next());
		}
	}

	protected void populate_LiteralBox(int index) {
		property_Literals.clear();
		property_Literals.addItem("NONE");
		Iterator<String> it = ontology.get(index).getLiterals().iterator();
		while (it.hasNext()) {
			property_Literals.addItem(it.next());
		}
	}

	protected void print_Ontology(String ontClass, ListBox box) {
		// TODO Auto-generated method stub
		box.addItem(ontClass);
	}

	@SuppressWarnings("deprecation")
	protected void printSubject() {

		subject = webBox.getText().concat("/" + content.getText().replace(' ', '_'));

		logger.log(Level.SEVERE, "rowcount:" + row);
		content.getText().replace(' ', '_');
		if (radioA.isChecked() || !radioB.isChecked()) {
			row = tripleTable.getRowCount();
			if ((tripleTable.getText(row - 1, 2).isEmpty() || (!tripleTable.getText(row - 1, 2).isEmpty() && tripleTable.getText(row - 1, 0)
					.isEmpty()))) {
				row--;
			} else if (tripleTable.getText(row - 1, 0).isEmpty())
				row--;
			tripleTable.setText(row, 0, subject);

		} else {
			// Adding literal object
			String obj = content.getText().replace(' ', '_');
			if (!tripleTable.getText(row, 1).isEmpty()) {
				if (tripleTable.getText(row, 1).endsWith("*"))
					tripleTable.setText(row, 2, obj);
				else {
					Window.alert("Must select from Ontology Classes, predicate is an Object Property");
				}
			} else {
				tripleTable.setText(row, 2, obj);
			}
			return;
		}

		tripleTable.setWidget(0, 5, save);
		Button removeButton = new Button("x"); // Will remove a triple from the
												// list
		Button suggestion = new Button("Suggestions");
		tripleTable.setWidget(row, 3, removeButton);

		tripleTable.setWidget(row, 4, suggestion);
		removeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(final ClickEvent event) {
				tripleTable.addClickHandler(new ClickHandler() {

					@Override
					public void onClick(ClickEvent click) {
						com.google.gwt.user.client.ui.HTMLTable.Cell cell = tripleTable.getCellForEvent(event);
						int cellIndex = cell.getCellIndex();
						int rowIndex = cell.getRowIndex();
						logger.log(Level.SEVERE, "cell:" + cellIndex);
						// if (cellIndex == 3) {
						// tripleTable.removeRow(rowIndex);
						// }
					}

				});
			}

		});

	}

	protected void addLitObject(String item) {
		tripleTable.setText(row, 2, item);
	}

	/*
	 * Adding Class Object to triple table
	 */
	protected void addObject(String item) {
		String p = null;
		if (tripleTable.getText(row - 1, 2).isEmpty())
			row--;

		if (tripleTable.getText(row, 1).isEmpty()) {
			tripleTable.setText(row, 2, item);
		} else {
			p = tripleTable.getText(row, 1);
			p = p.substring(p.indexOf('#') + 1, p.length());
			if (ontology.get(ontologies.getSelectedIndex()).getProperties().contains(p) || p.equals("RDF.type")) {
				tripleTable.setText(row, 2, item);
			} else {
				if (item.startsWith("http://")) {
					Window.alert("Must enter a literal value");
				} else {
					tripleTable.setText(row, 2, item);
				}
			}
		}
	}

	/*
	 * Adding either a O.Prop or D.Prop to prediate in triple table
	 */
	protected void addPredicate(String item) {
		String o = null;
		if (tripleTable.getText(row, 2).length() > 0) {
			o = tripleTable.getText(row, 2);
			o = o.substring(o.indexOf('#') + 1, o.length());

			logger.log(Level.SEVERE, "This should be a resource predicate: " + ontology.get(ontologies.getSelectedIndex()).getClasses().contains(o));
			if (ontology.get(ontologies.getSelectedIndex()).getClasses().contains(o)) {
				if (item.endsWith("*")) {
					Window.alert("Must select Object-Property. Object is a resource");
				} else {
					tripleTable.setText(row, 1, item);
				}
			} else {
				if (item.endsWith("*")) {
					tripleTable.setText(row, 1, item);
				} else {
					Window.alert("Must select Literal-Property");
				}
			}
		} else {
			tripleTable.setText(row, 1, item);
		}
	}

	/*
	 * Get an arrayList of triple java objects to be sent to server side which
	 * in turn to be sent to triple store -> server
	 */
	protected String[] getTriples() {
		String[] contents = new String[3];
		String message = "";
		int rowcount = tripleTable.getRowCount();
		logger.log(Level.SEVERE, tripleTable.getText(0, 0));
		while (rowcount > 1) {
			logger.log(Level.SEVERE, "Rowcount is: " + rowcount);

			contents[0] = (tripleTable.getText(rowcount - 1, 0));
			contents[1] = (tripleTable.getText(rowcount - 1, 1));
			contents[2] = (tripleTable.getText(rowcount - 1, 2));
			message += "\nSubject: " + contents[0] + "\nPredicate: " + contents[1] + "\nObject: " + contents[2];
			logger.log(Level.SEVERE, contents[1]);
			tripleTable.removeRow(rowcount - 1);

			final AsyncCallback<String[]> sendToTripleStore = new AsyncCallback<String[]>() {

				@Override
				public void onFailure(Throwable caught) {
					// TODO Auto-generated method stub
					Window.alert("FAILED TO UPLOAD");
				}

				@Override
				public void onSuccess(String[] result) {
					// TODO Auto-generated method stub
					Window.alert("UPLOADED");
				}

			};
			greetingService.sendToTripleStore(contents, sendToTripleStore);
			logger.log(Level.SEVERE, "Uploaded a triple");
			rowcount = tripleTable.getRowCount();
			// logger.log(Level.SEVERE, "rowcount= " + rowcount);
		}

		// StringEscapeUtils seu = new StringEscapeUtils();
		HTML triples_sent = new HTML(message);
		dialogBoxContents.add(triples_sent);
		dialogBoxContents.add(close);
		dBox.setWidget(dialogBoxContents);
		dBox.center();
		// return new_triple_list;
		return contents;
	}

	protected void populateSuggestedTriples(List<String[]> action) {
		logger.log(Level.SEVERE, "Size of sugT" + action.size());
		Iterator<String[]> it = action.iterator();
		while (it.hasNext()) {
			String temp[] = new String[3];
			temp = it.next();
			logger.log(Level.SEVERE, temp[0] + " " + temp[1] + " " + temp[2]);

			ft.setText(0, 0, "Subject");
			ft.setText(0, 1, "Predicate");
			ft.setText(0, 2, "Object");
			ft.setText(0, 3, "Add");
			int rcount = ft.getRowCount();
			ft.setText(rcount, 0, temp[0]);
			ft.setText(rcount, 1, temp[1]);
			ft.setText(rcount, 2, temp[2]);

			cb = new CheckBox("Add");
			// cb.setChecked(true);
			cb.setValue(false);
			ft.setWidget(rcount, 3, cb);

			cb.addClickHandler(new ClickHandler() {

				@SuppressWarnings("deprecation")
				@Override
				public void onClick(ClickEvent event) {
					boolean checked = ((CheckBox) event.getSource()).isChecked();
				}

			});
			ft.addClickHandler(new ClickHandler() {
				int count = 0;

				@Override
				public void onClick(ClickEvent event) {
					// int count = 0;
					if (count < 1) {
						com.google.gwt.user.client.ui.HTMLTable.Cell cell = ft.getCellForEvent(event);
						int cellIndex = cell.getCellIndex();
						int rowIndex = cell.getRowIndex();
						logger.log(Level.SEVERE, "cell:" + cellIndex + "~ Row:" + (rowIndex));
						if (cellIndex == 3 && cb.getValue()) {
						}
					}
					count++;
				}

			});
		}
		Label lb = new Label();
		popupContents.add(popupHolder);
		popupContents.add(ft);
		lb.setText("* defines a Literal value");
		popupContents.add(lb);
		popup.center();

	}

	@SuppressWarnings("deprecation")
	protected void loadPageTwo(String path) {
		rootPanel.setVisible(false);

		/* second page */
		final String export_path = path;
		queryPanel.add(ontology_label);
		queryPanel.add(ontologies);
		queryPanel.add(classes_label);
		queryPanel.add(ontology_Classes);
		queryPanel.add(object_prop_label);
		queryPanel.add(property_Resources);
		queryPanel.add(data_prop_label);
		queryPanel.add(property_Literals);
		queryPanel.add(subject_label);
		queryPanel.add(subjectQuery);
		queryPanel.add(queryButton);
		root2Panel.setVisible(true);

		buildTree(export_path);
		greetingService.getChildren(export_path, "Thing", new TreeRootCallback(browseTree));
		// Gets instances for selected tree item!
		browseTree.addTreeListener(new TreeListener() {

			@Override
			public void onTreeItemSelected(TreeItem item) {
				logger.log(Level.SEVERE, "Item = " + item.getText());
				instance_grid.removeAllRows();
				greetingService.getInstances(export_path, item.getText(), entercontext.getText(), new TreeItemInstances());
			}

			@Override
			public void onTreeItemStateChanged(TreeItem item) {

			}

		});

		instance_grid.setText(0, 0, "Row 1:Col 1");
		root2Panel.add(home_page);
		root2Panel.add(page2Panel);
		int left2, top2;
		left2 = Window.getClientWidth() / 5;
		top2 = Window.getClientHeight() / 5;
	}

	protected void buildTree(String path) {
		TreeItem root = new TreeItem(LOADING_ITEMS);
		browseTree.addItem(root);
		final String ont = path;
		browseTree.addOpenHandler(new OpenHandler<TreeItem>() {

			@Override
			public void onOpen(OpenEvent<TreeItem> event) {
				if (needsLoading(event.getTarget())) {

					greetingService.getChildren(ont, event.getTarget().getText(), new TreeItemCallback(event.getTarget()));
				}
			}
		});
		logger.log(Level.SEVERE, "Tree Built");
	}

	protected boolean needsLoading(TreeItem item) {
		return item.getChildCount() == 1 && LOADING_ITEMS.equals(item.getChild(0).getText());
	}

	public final class queryInstances implements AsyncCallback<ArrayList<String[]>> {
		public void onFailure(Throwable caught) {

		}

		public void onSuccess(ArrayList<String[]> result) {
			Window.alert("GOT INSTANCES BACK");
			instance_grid.removeAllRows();
			int grid_row = 0;
			Iterator<String[]> ii = result.iterator();
			while (ii.hasNext()) {
				String[] temp = ii.next();
				Anchor c = new Anchor(temp[0]);
				for (int i = 0; i < temp.length; i++) {
					if (i == 0) {
						logger.log(Level.SEVERE, "Setting anchor widget to table");
						instance_grid.setWidget(grid_row, i, c);
						c.addClickHandler(link_to_page);
					} else {
						logger.log(Level.SEVERE, "Not: " + temp[i]);
						instance_grid.setText(grid_row, i, temp[i]);
					}
				}
				grid_row++;
			}
		}
	}

	public final class downloadRepository implements AsyncCallback<String> {

		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			Window.alert("fail on download repository");
		}

		public void onSuccess(String names) {
			Window.alert(names);
			export_fp = names;
			// ontology.add(new Ontology("Export", export_fp, null, null, null,
			// null));
			// ontologies.addItem(ontology.get(ontology.size() - 1).getName());
			// ontologies.setSelectedIndex(ontology.size() - 1);
			if (repository_downloaded)
				loadPageTwo(export_fp);
		}
	}

	public static final class TreeRootCallback implements AsyncCallback<ArrayList<String>> {

		private Tree browseTree;

		public TreeRootCallback(Tree browseTree) {
			super();
			this.browseTree = browseTree;
		}

		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			Window.alert("Fail on tree root callback");
		}

		public void onSuccess(ArrayList<String> names) {
			browseTree.removeItems();
			for (String name : names) {
				logger.log(Level.SEVERE, "Gotten first rot of tree");
				TreeItem ti = new TreeItem(name);
				ti.addItem(LOADING_ITEMS);
				browseTree.addItem(ti);
			}
		}

	}

	public static final class TreeItemCallback implements AsyncCallback<ArrayList<String>> {

		private TreeItem treeItem;

		public TreeItemCallback(TreeItem treeItem) {
			super();
			this.treeItem = treeItem;
		}

		public void onFailure(Throwable caught) {
			caught.printStackTrace();
			Window.alert("fail on tree item callback");
		}

		public void onSuccess(ArrayList<String> names) {
			treeItem.removeItems();
			for (String name : names) {
				TreeItem ti = new TreeItem(name);
				ti.addItem(LOADING_ITEMS);
				treeItem.addItem(ti);
			}
		}
	}

	public final class TreeItemInstances implements AsyncCallback<ArrayList<String[]>> {

		@Override
		public void onFailure(Throwable caught) {

		}

		@Override
		public void onSuccess(ArrayList<String[]> result) {
			logger.log(Level.SEVERE, "Instances size: " + result.size());
			Window.alert("GOT INSTANCES BACK");
			instance_grid.clear();
			int grid_row = 0;
			Iterator<String[]> ii = result.iterator();
			while (ii.hasNext()) {
				String[] temp = ii.next();
				Anchor c = new Anchor(temp[0]);
				c.addClickHandler(link_to_page);
				for (int i = 0; i < temp.length; i++) {
					if (i == 0) {
						logger.log(Level.SEVERE, "Setting anchor widget to table");
						instance_grid.setWidget(grid_row, i, c);
					} else {
						logger.log(Level.SEVERE, "Not: " + temp[i]);
						instance_grid.setText(grid_row, i, temp[i]);
					}
				}
				grid_row++;
			}
		}

	}

	protected void loadHomePage() {
		root2Panel.setVisible(false);
		rootPanel.setVisible(true);

		Ont_Table.setText(3, 0, "Class"); // 3 columns
		Ont_Table.setText(3, 1, "Object Property");
		Ont_Table.setText(3, 2, "Data Property");

		Ont_Table.setWidget(0, 1, lblOntologies);
		lblOntologies.setSize("100%", "100%");
		Ont_Table.setWidget(1, 1, ontologies);

		Ont_Table.setWidget(2, 0, lblClasses);
		Ont_Table.setWidget(2, 1, lblObjectProperties);
		Ont_Table.setWidget(2, 2, lblDataProperties);

		Ont_Table.setWidget(3, 0, ontology_Classes);
		Ont_Table.setWidget(3, 1, property_Resources);
		Ont_Table.setWidget(3, 2, property_Literals);

		browseTree.clear();

	}

	/*
	 * Handles URL changes within the iFrame
	 */
	protected native String urlChange(Frame iframe)/*-{
		if (iframe.contentDocument !== undefined) {
			if (iframe.contentDocument.defaultView !== undefined
					&& iframe.contentDocument.defaultView.location !== undefined) {
				return iframe.contentDocument.defaultView.location.href;
			} else {
				return iframe.contentDocument.URL;
			}
		} else if (iframe.contentWindow !== undefined
				&& iframe.contentWindow.document !== undefined) {
			return iframe.contentWindow.document;
		} else {
			return "failed to get new URL";
		}
	}-*/;

	public native void alert(String msg) /*-{
		$wnd.alert(msg);
	}-*/;

	// @Override
	// public void onBrowserEvent(Event event) {
	// if(DOM.eventGetType(event) == Event.ONLOAD){
	// logger.log(Level.SEVERE, "Event type:" + event);
	// }
	// }
}