import React, { useState, useEffect } from 'react';
import axios from 'axios';
import Table from '@mui/material/Table';
import TableBody from '@mui/material/TableBody';
import TableCell from '@mui/material/TableCell';
import TableContainer from '@mui/material/TableContainer';
import TableHead from '@mui/material/TableHead';
import TableRow from '@mui/material/TableRow';
import Paper from '@mui/material/Paper';
import IconButton from '@mui/material/IconButton';
import RefreshIcon from '@mui/icons-material/Refresh';
import CircularProgress from '@mui/material/CircularProgress';

export default function BasicTable() {
  const [data, setData] = useState([] );
  const [loading, setLoading] = useState(false);

  async function fetchData() {
    // You can await here
    setLoading(true);
    const result = await axios(
      //'https://tc-func-app1.azurewebsites.net/api/listTitusAuditLogs',
      'http://localhost:7071/api/listTitusAuditLogs',
    );
    setData(result.data);
    setLoading(false);
  }

  useEffect(() => {
    fetchData();    
  }, []);

  return (
    <>
      <IconButton color="primary" aria-label="upload picture" component="span" 
        onClick={() => { fetchData(); }}>
            <RefreshIcon />
      </IconButton>
      {loading ? <CircularProgress size={14} /> : <TableContainer component={Paper}>
        <Table sx={{ minWidth: 650 }} aria-label="simple table">
          <TableHead>
            <TableRow>
              <TableCell>Document Name</TableCell>
              <TableCell align="right">User Name</TableCell>
              <TableCell align="right">Access Type</TableCell>
              <TableCell align="right">App Version</TableCell>
              <TableCell align="right">Contains CUI</TableCell>
              <TableCell>Contains ECI</TableCell>
              <TableCell align="right">Designation</TableCell>
              <TableCell align="right">Doc Security</TableCell>
              <TableCell align="right">Hyperlinks Changed</TableCell>
              <TableCell align="right">Links Up To Date</TableCell>
              <TableCell>Proprietary</TableCell>
              <TableCell align="right">Scale Crop</TableCell>
              <TableCell align="right">Share Doc</TableCell>
              <TableCell align="right">Titus GUID</TableCell>
              <TableCell align="right">Visual Mark</TableCell>
              <TableCell align="right">Logged Time</TableCell>
            </TableRow>
          </TableHead>
          <TableBody>
            {data.map((row) => (
              <TableRow
                key={row.documentName}
                sx={{ '&:last-child td, &:last-child th': { border: 0 } }}
              >
                <TableCell component="th" scope="row">
                  {row.documentName}
                </TableCell>
                <TableCell align="right">{row.userName}</TableCell>
                <TableCell align="right">{row.accessType}</TableCell>
                <TableCell align="right">{row.appVersion}</TableCell>
                <TableCell align="right">{row.containsCUI}</TableCell>
                <TableCell align="right">{row.containsECI}</TableCell>
                <TableCell align="right">{row.designation}</TableCell>
                <TableCell align="right">{row.docSecurity}</TableCell>
                <TableCell align="right">{row.hyperlinksChanged}</TableCell>
                <TableCell align="right">{row.linksUpToDate}</TableCell>
                <TableCell align="right">{row.proprietary}</TableCell>
                <TableCell align="right">{row.scaleCrop}</TableCell>
                <TableCell align="right">{row.shareDoc}</TableCell>
                <TableCell align="right">{row.titusGUID}</TableCell>
                <TableCell align="right">{row.visualMark}</TableCell>
                <TableCell align="right">{row.loggedTime}</TableCell>
              </TableRow>
            ))}
          </TableBody>
        </Table>
      </TableContainer>}
    </>
  );
}