+ ArrayedCollection{

      removeAtIndexes{arg indexes;
                indexes.sort.reverse.do{arg index;
          this.removeAt(index);
      }
  }
}