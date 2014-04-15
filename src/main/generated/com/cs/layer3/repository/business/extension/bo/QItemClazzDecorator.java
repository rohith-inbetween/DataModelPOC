package com.cs.layer3.repository.business.extension.bo;

import static com.mysema.query.types.PathMetadataFactory.*;

import com.mysema.query.types.path.*;

import com.mysema.query.types.PathMetadata;
import javax.annotation.Generated;
import com.mysema.query.types.Path;
import com.mysema.query.types.path.PathInits;


/**
 * QItemClazzDecorator is a Querydsl query type for ItemClazzDecorator
 */
@Generated("com.mysema.query.codegen.EntitySerializer")
public class QItemClazzDecorator extends EntityPathBase<ItemClazzDecorator> {

    private static final long serialVersionUID = 448153411L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QItemClazzDecorator itemClazzDecorator = new QItemClazzDecorator("itemClazzDecorator");

    public final com.cs.layer3.repository.business.defalt.bo.QBusinessObject _super = new com.cs.layer3.repository.business.defalt.bo.QBusinessObject(this);

    public final com.cs.layer3.repository.business.defalt.bo.QClazz clazz;

    public final com.cs.layer3.repository.business.defalt.bo.QEntity entity;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    //inherited
    public final SimplePath<Iterable> referencedList = _super.referencedList;

    public QItemClazzDecorator(String variable) {
        this(ItemClazzDecorator.class, forVariable(variable), INITS);
    }

    public QItemClazzDecorator(Path<? extends ItemClazzDecorator> path) {
        this(path.getType(), path.getMetadata(), path.getMetadata().isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QItemClazzDecorator(PathMetadata<?> metadata) {
        this(metadata, metadata.isRoot() ? INITS : PathInits.DEFAULT);
    }

    public QItemClazzDecorator(PathMetadata<?> metadata, PathInits inits) {
        this(ItemClazzDecorator.class, metadata, inits);
    }

    public QItemClazzDecorator(Class<? extends ItemClazzDecorator> type, PathMetadata<?> metadata, PathInits inits) {
        super(type, metadata, inits);
        this.clazz = inits.isInitialized("clazz") ? new com.cs.layer3.repository.business.defalt.bo.QClazz(forProperty("clazz")) : null;
        this.entity = inits.isInitialized("entity") ? new com.cs.layer3.repository.business.defalt.bo.QEntity(forProperty("entity")) : null;
    }

}

