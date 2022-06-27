import React from 'react';
import PivotTableUI from 'react-pivottable/PivotTableUI';
import 'react-pivottable/pivottable.css';
import createPlotlyComponent from 'react-plotly.js/factory';
import createPlotlyRenderers from 'react-pivottable/PlotlyRenderers';
import TableRenderers from 'react-pivottable/TableRenderers';

// create Plotly React component via dependency injection
const Plot = createPlotlyComponent(window.Plotly);

// create Plotly renderers via dependency injection
const PlotlyRenderers = createPlotlyRenderers(Plot);
// see documentation for supported input formats
const data = [{"id":235,"documentName":"61622-3.docx","userName":"Matt Henson","accessType":"CREATED","loggedTime":"2022-06-27T17:29:46.538+00:00","eci":"Yes","eciCoC":"US","eciJuris":"ITAR","eciClass":"XI(d)","export":"YES","exAuth":"TA12345-01","containsCUI":"Yes","cui":"SP-CTI;SP-EXPT","dissemination":"DISPLAYONLY","proprietary":"No","proprietaryType":"","proprietaryStatement":""},
              {"id":234,"documentName":"61622-3.docx","userName":"Matt Henson","accessType":"UPDATED","loggedTime":"2022-06-27T17:29:46.534+00:00","eci":"Yes","eciCoC":"US","eciJuris":"ITAR","eciClass":"XI(d)","export":"YES","exAuth":"TA12345-01","containsCUI":"Yes","cui":"SP-CTI;SP-EXPT","dissemination":"DISPLAYONLY","proprietary":"No","proprietaryType":"","proprietaryStatement":""},
              {"id":236,"documentName":"5-31-22-1.docx","userName":"Matt Henson","accessType":"READ","loggedTime":"2022-06-27T17:28:59.000+00:00","eci":null,"eciCoC":null,"eciJuris":null,"eciClass":null,"export":null,"exAuth":null,"containsCUI":null,"cui":null,"dissemination":null,"proprietary":null,"proprietaryType":null,"proprietaryStatement":null},
              {"id":233,"documentName":"5-31-22-1.docx","userName":"Matt Henson","accessType":"READ","loggedTime":"2022-06-27T14:53:46.000+00:00","eci":null,"eciCoC":null,"eciJuris":null,"eciClass":null,"export":null,"exAuth":null,"containsCUI":null,"cui":null,"dissemination":null,"proprietary":null,"proprietaryType":null,"proprietaryStatement":null},
              {"id":232,"documentName":"53122-3.docx","userName":"Matt Henson","accessType":"READ","loggedTime":"2022-06-27T14:50:15.000+00:00","eci":null,"eciCoC":null,"eciJuris":null,"eciClass":null,"export":null,"exAuth":null,"containsCUI":null,"cui":null,"dissemination":null,"proprietary":null,"proprietaryType":null,"proprietaryStatement":null},
              {"id":231,"documentName":"5-26-22_FullECI_FullCUI.docx","userName":"Dave Harris","accessType":"READ","loggedTime":"2022-06-27T14:50:01.000+00:00","eci":null,"eciCoC":null,"eciJuris":null,"eciClass":null,"export":null,"exAuth":null,"containsCUI":null,"cui":null,"dissemination":null,"proprietary":null,"proprietaryType":null,"proprietaryStatement":null},
              {"id":230,"documentName":"5-26-22_FullECI_FullCUI-1.docx","userName":"Krishna Prasad","accessType":"READ","loggedTime":"2022-06-26T23:48:03.000+00:00","eci":null,"eciCoC":null,"eciJuris":null,"eciClass":null,"export":null,"exAuth":null,"containsCUI":null,"cui":null,"dissemination":null,"proprietary":null,"proprietaryType":null,"proprietaryStatement":null},
              {"id":229,"documentName":"5-26-22_FullECI_FullCUI-1.docx","userName":"Krishna Prasad","accessType":"UPDATED","loggedTime":"2022-06-26T23:38:48.267+00:00","eci":"Yes","eciCoC":"US","eciJuris":"ITAR","eciClass":"XI(d)","export":"YES","exAuth":"TA12345-01","containsCUI":"Yes","cui":"SP-CTI;SP-EXPT","dissemination":"DISPLAYONLY","proprietary":"Yes","proprietaryType":"3rdParty","proprietaryStatement":"Lockheed"},
              {"id":228,"documentName":"5-26-22_FullECI_FullCUI-1.docx","userName":"Krishna Prasad","accessType":"CREATED","loggedTime":"2022-06-26T23:38:30.921+00:00","eci":"Yes","eciCoC":"US","eciJuris":"ITAR","eciClass":"XI(d)","export":"YES","exAuth":"TA12345-01","containsCUI":"Yes","cui":"SP-CTI;SP-EXPT","dissemination":"DISPLAYONLY","proprietary":"Yes","proprietaryType":"3rdParty","proprietaryStatement":"Lockheed"},
              {"id":227,"documentName":"5-26-22_FullECI_FullCUI-1.docx","userName":"Krishna Prasad","accessType":"DELETED","loggedTime":"2022-06-26T23:38:04.778+00:00","eci":null,"eciCoC":null,"eciJuris":null,"eciClass":null,"export":null,"exAuth":null,"containsCUI":null,"cui":null,"dissemination":null,"proprietary":null,"proprietaryType":null,"proprietaryStatement":null},
              {"id":226,"documentName":"5-26-22_FullECI_FullCUI.docx","userName":"Krishna Prasad","accessType":"READ","loggedTime":"2022-06-26T23:34:02.000+00:00","eci":null,"eciCoC":null,"eciJuris":null,"eciClass":null,"export":null,"exAuth":null,"containsCUI":null,"cui":null,"dissemination":null,"proprietary":null,"proprietaryType":null,"proprietaryStatement":null},
              {"id":220,"documentName":"5-26-22_FullECI_FullCUI.docx","userName":"Krishna Prasad","accessType":"UPDATED","loggedTime":"2022-06-26T23:29:41.933+00:00","eci":"Yes","eciCoC":"US","eciJuris":"ITAR","eciClass":"XI(d)","export":"YES","exAuth":"TA12345-01","containsCUI":"Yes","cui":"SP-CTI;SP-EXPT","dissemination":"DISPLAYONLY","proprietary":"Yes","proprietaryType":"3rdParty","proprietaryStatement":"Lockheed"},
              {"id":221,"documentName":"5-31-22-1.docx","userName":"Krishna Prasad","accessType":"UPDATED","loggedTime":"2022-06-26T23:29:41.927+00:00","eci":"Yes","eciCoC":"US","eciJuris":"ITAR","eciClass":"XI(d)","export":"YES","exAuth":"TA45678-03","containsCUI":"Yes","cui":"SP-CTI;SP-EXPT","dissemination":"DISPLAYONLY","proprietary":"No","proprietaryType":"","proprietaryStatement":""},
              {"id":222,"documentName":"5-26-22_FullECI_FullCUI.docx","userName":"Krishna Prasad","accessType":"CREATED","loggedTime":"2022-06-26T23:29:41.927+00:00","eci":"Yes","eciCoC":"US","eciJuris":"ITAR","eciClass":"XI(d)","export":"YES","exAuth":"TA12345-01","containsCUI":"Yes","cui":"SP-CTI;SP-EXPT","dissemination":"DISPLAYONLY","proprietary":"Yes","proprietaryType":"3rdParty","proprietaryStatement":"Lockheed"},
              {"id":219,"documentName":"5-31-22-1.docx","userName":"Krishna Prasad","accessType":"CREATED","loggedTime":"2022-06-26T23:29:41.926+00:00","eci":"Yes","eciCoC":"US","eciJuris":"ITAR","eciClass":"XI(d)","export":"YES","exAuth":"TA45678-03","containsCUI":"Yes","cui":"SP-CTI;SP-EXPT","dissemination":"DISPLAYONLY","proprietary":"No","proprietaryType":"","proprietaryStatement":""},
              {"id":223,"documentName":"5-26-22_FullECI_FullCUI-1.docx","userName":"Krishna Prasad","accessType":"UPDATED","loggedTime":"2022-06-26T23:29:41.926+00:00","eci":"Yes","eciCoC":"US","eciJuris":"ITAR","eciClass":"XI(d)","export":"YES","exAuth":"TA12345-01","containsCUI":"Yes","cui":"SP-CTI;SP-EXPT","dissemination":"DISPLAYONLY","proprietary":"Yes","proprietaryType":"3rdParty","proprietaryStatement":"Lockheed"},
              {"id":224,"documentName":"5-31-22-1.docx","userName":"Krishna Prasad","accessType":"READ","loggedTime":"2022-06-26T23:29:16.000+00:00","eci":null,"eciCoC":null,"eciJuris":null,"eciClass":null,"export":null,"exAuth":null,"containsCUI":null,"cui":null,"dissemination":null,"proprietary":null,"proprietaryType":null,"proprietaryStatement":null},
              {"id":225,"documentName":"5-26-22_FullECI_FullCUI-1.docx","userName":"Krishna Prasad","accessType":"READ","loggedTime":"2022-06-26T23:28:44.000+00:00","eci":null,"eciCoC":null,"eciJuris":null,"eciClass":null,"export":null,"exAuth":null,"containsCUI":null,"cui":null,"dissemination":null,"proprietary":null,"proprietaryType":null,"proprietaryStatement":null},
              {"id":217,"documentName":"53122-3.docx","userName":"Krishna Prasad","accessType":"READ","loggedTime":"2022-06-26T23:08:18.000+00:00","eci":null,"eciCoC":null,"eciJuris":null,"eciClass":null,"export":null,"exAuth":null,"containsCUI":null,"cui":null,"dissemination":null,"proprietary":null,"proprietaryType":null,"proprietaryStatement":null},
              {"id":218,"documentName":"5-26-22_FullECI_FullCUI-1.docx","userName":"Krishna Prasad","accessType":"READ","loggedTime":"2022-06-26T23:08:09.000+00:00","eci":null,"eciCoC":null,"eciJuris":null,"eciClass":null,"export":null,"exAuth":null,"containsCUI":null,"cui":null,"dissemination":null,"proprietary":null,"proprietaryType":null,"proprietaryStatement":null},
              {"id":216,"documentName":"5-26-22_FullECI_FullCUI-1.docx","userName":"Krishna Prasad","accessType":"CREATED","loggedTime":"2022-06-26T20:40:42.315+00:00","eci":"Yes","eciCoC":"US","eciJuris":"ITAR","eciClass":"XI(d)","export":"YES","exAuth":"TA12345-01","containsCUI":"Yes","cui":"SP-CTI;SP-EXPT","dissemination":"DISPLAYONLY","proprietary":"Yes","proprietaryType":"3rdParty","proprietaryStatement":"Lockheed"},
              {"id":215,"documentName":"53122-3.docx","userName":"Krishna Prasad","accessType":"CREATED","loggedTime":"2022-06-26T20:40:42.307+00:00","eci":"Yes","eciCoC":"US","eciJuris":"ITAR","eciClass":"XI(d)","export":"YES","exAuth":"TA12345-01","containsCUI":"Yes","cui":"SP-CTI;SP-EXPT","dissemination":"DISPLAYONLY","proprietary":"No","proprietaryType":"","proprietaryStatement":""},
              {"id":214,"documentName":"53122-3.docx","userName":"Krishna Prasad","accessType":"CREATED","loggedTime":"2022-06-26T20:37:33.138+00:00","eci":"Yes","eciCoC":"US","eciJuris":"ITAR","eciClass":"XI(d)","export":"YES","exAuth":"TA12345-01","containsCUI":"Yes","cui":"SP-CTI;SP-EXPT","dissemination":"DISPLAYONLY","proprietary":"No","proprietaryType":"","proprietaryStatement":""},
              {"id":213,"documentName":"5-26-22_FullECI_FullCUI-1.docx","userName":"Krishna Prasad","accessType":"CREATED","loggedTime":"2022-06-26T20:37:32.834+00:00","eci":"Yes","eciCoC":"US","eciJuris":"ITAR","eciClass":"XI(d)","export":"YES","exAuth":"TA12345-01","containsCUI":"Yes","cui":"SP-CTI;SP-EXPT","dissemination":"DISPLAYONLY","proprietary":"Yes","proprietaryType":"3rdParty","proprietaryStatement":"Lockheed"}];

const pivotPresets = {
  cols: ["eciJuris"],
  rows: ["documentName", "userName"],
  rendererName: "Table Heatmap",
  aggregatorName: "Count",
  vals: ["accessType"],
  //derivedAttributes: { completeName: el => el.name + " " + el.surname }
};

class App extends React.Component {
  constructor(props) {
      super(props);
      this.state = props;
  }

  render() {
      return (
          <PivotTableUI
              data={data}
              onChange={s => this.setState(s)}
              renderers={Object.assign({}, TableRenderers, PlotlyRenderers)}
              {...pivotPresets}
              {...this.state}
          />
      );
  }
}

export default App;