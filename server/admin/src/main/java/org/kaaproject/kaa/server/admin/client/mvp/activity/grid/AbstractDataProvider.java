/*
 * Copyright 2014 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaaproject.kaa.server.admin.client.mvp.activity.grid;

import java.util.Collections;
import java.util.List;

import org.kaaproject.avro.ui.gwt.client.widget.grid.AbstractGrid;
import org.kaaproject.kaa.server.admin.client.util.HasErrorMessage;
import org.kaaproject.kaa.server.admin.client.util.Utils;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent;
import com.google.gwt.user.cellview.client.ColumnSortList;
import com.google.gwt.view.client.AsyncDataProvider;
import com.google.gwt.view.client.HasData;

public abstract class AbstractDataProvider<T> extends AsyncDataProvider<T> implements ColumnSortEvent.Handler {

    protected List<T> data;

    private boolean loaded = false;

    private LoadCallback callback;

    private AbstractGrid<T,?> dataGrid;
    
    public AbstractDataProvider(AbstractGrid<T,?> dataGrid, HasErrorMessage hasErrorMessage)
    {
        this(dataGrid, hasErrorMessage, true);
    }

    public AbstractDataProvider(AbstractGrid<T,?> dataGrid, HasErrorMessage hasErrorMessage, boolean addDisplay)
    {
        this.dataGrid = dataGrid;
        callback = new LoadCallback(hasErrorMessage);
        dataGrid.getDataGrid().addColumnSortHandler(this);
        if (addDisplay) {
            addDataDisplay(dataGrid.getDataGrid());
        }
    }
    
    protected void addDataDisplay() {
        addDataDisplay(dataGrid.getDataGrid());
    }

    public void addRow(T row) {
        data.add(row);
        updateRowCount(data.size(), true);
        updateRowData(data.size()-1, data.subList(data.size()-1, data.size()));
    }

    public void updateRow(T row) {
        int index = data.indexOf(row);
        updateRowData(index, data.subList(index, index+1));
    }

    public List<T> getData() {
        return data;
    }

    public void setLoaded(boolean loaded) {
        this.loaded = loaded;
    }

    public void reload() {
        this.loaded = false;
        loadData(callback);
    }

    @Override
    protected void onRangeChanged(final HasData<T> display) {
      if (!loaded) {
          loadData(callback);
      }
      else {
          updateData();
      }
    }

    protected abstract void loadData(final LoadCallback callback);
    
    @Override
    public void onColumnSort(ColumnSortEvent event) {
        Column<?,?> column = event.getColumn();
        boolean isSortAscending = event.isSortAscending();
        if (column != null) {
            dataGrid.sort(data, column, isSortAscending);
        }
        updateRowData(0, data);
    }

    private void updateData () {
        ColumnSortList sortList = dataGrid.getDataGrid().getColumnSortList();
        Column<?,?> column = (sortList == null || sortList.size() == 0) ? null
                : sortList.get(0).getColumn();
        boolean isSortAscending = (sortList == null || sortList.size() == 0) ? false
                : sortList.get(0).isAscending();
        if (column != null) {
            dataGrid.sort(data, column, isSortAscending);
        }        
        updateRowData(0, data);
    }

    public class LoadCallback {

        private HasErrorMessage hasErrorMessage;

        public LoadCallback(HasErrorMessage hasErrorMessage) {
            this.hasErrorMessage = hasErrorMessage;
        }

        public void onFailure(Throwable caught) {
            GWT.log("AbstractDataProvider.LoadCallback.onFailure(caught):", caught);
            Utils.handleException(caught, hasErrorMessage);
        }

        public void onSuccess(List<T> result) {
            dataGrid.getSelectionModel().clear();
            data = result;
            if (data == null) {
                data = Collections.<T>emptyList();
            }
            updateRowCount(data.size(), true);
            updateData();
            loaded = true;
            hasErrorMessage.clearError();
        }
    }
}
