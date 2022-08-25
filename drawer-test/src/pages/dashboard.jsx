import React, { useState, useEffect } from 'react';
import PivotTableUI from 'react-pivottable/PivotTableUI';
import 'react-pivottable/pivottable.css';
import createPlotlyComponent from 'react-plotly.js/factory';
import createPlotlyRenderers from 'react-pivottable/PlotlyRenderers';
import TableRenderers from 'react-pivottable/TableRenderers';
import axios from 'axios';
import CircularProgress from '@mui/material/CircularProgress';

// create Plotly React component via dependency injection
const Plot = createPlotlyComponent(window.Plotly);

// create Plotly renderers via dependency injection
const PlotlyRenderers = createPlotlyRenderers(Plot);

const pivotPresets = {
  cols: ['eciJuris'],
  rows: ['userId', 'documentName', 'accessType'],
  rendererName: 'Table Heatmap',
  aggregatorName: 'Count',
  vals: ['accessType'],
};

export default function Dashboard(props) {
  const [state, setState] = useState(props);
  const [data, setData] = useState([]);
  const [loading, setLoading] = useState(false);

  async function fetchData(checkForChanges) {
    // You can await here
    setLoading(true);
    const result = await axios(
      //'https://tc-func-app1.azurewebsites.net/api/listTitusAuditLogs',
      '/api/listTitusAuditLogs2?checkForChanges=' + checkForChanges
    );
    setData(result.data);
    setLoading(false);
  }

  useEffect(() => {
    fetchData(false);
  }, []);

  return (
    <>
      {loading ? (
        <CircularProgress size={14} />
      ) : (
        <PivotTableUI
          data={data}
          onChange={(s) => setState(s)}
          renderers={Object.assign({}, TableRenderers, PlotlyRenderers)}
          {...pivotPresets}
          {...state}
        />
      )}
    </>
  );
}
