package com.labshigh.martinipool.core.models;

import lombok.Getter;
import lombok.Setter;

public class ResponseListModel {

  @Getter
  private int totalCount;
  @Getter
  private int currentPage;
  @Getter
  private int pageSize;
  @Getter @Setter
  private Object list;

  @Getter
  private int totalPage = 0;

  public void setTotalCount(int totalCount) {
    this.totalCount = totalCount;
    setTotalPage();
  }

  public void setCurrentPage(int currentPage) {
    this.currentPage = currentPage;
    setTotalPage();
  }

  public void setPageSize(int pageSize) {
    this.pageSize = pageSize;
    setTotalPage();
  }

  public int setTotalPage() {
    if (totalCount == 0 || pageSize == 0) {
      return 0;
    }
    totalPage = totalCount % pageSize == 0 ? totalCount / pageSize : (totalCount / pageSize) + 1;
    return totalPage;
  }

}
